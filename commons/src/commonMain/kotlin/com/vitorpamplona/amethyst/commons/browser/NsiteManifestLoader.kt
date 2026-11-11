/*
 * Copyright (c) 2025 Vitor Pamplona
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.vitorpamplona.amethyst.commons.browser

import com.vitorpamplona.amethyst.commons.model.Note
import com.vitorpamplona.amethyst.commons.model.cache.LocalCache
import com.vitorpamplona.amethyst.commons.relayClient.event.EventFinderQueryState
import com.vitorpamplona.amethyst.commons.relayClient.reqCommand.RelaySubscriptionsCoordinator
import com.vitorpamplona.amethyst.commons.relayClient.user.UserFinderAccount
import com.vitorpamplona.amethyst.commons.relayClient.user.UserFinderQueryState
import com.vitorpamplona.quartz.nip01Core.core.Address
import com.vitorpamplona.quartz.nip01Core.core.Event
import com.vitorpamplona.quartz.nip19Bech32.entities.NAddress
import com.vitorpamplona.quartz.nip19Bech32.entities.NEvent
import com.vitorpamplona.quartz.nip19Bech32.entities.NNote
import com.vitorpamplona.quartz.nip19Bech32.entities.NProfile
import com.vitorpamplona.quartz.nip19Bech32.entities.NPub
import com.vitorpamplona.quartz.nip5aStaticWebsites.RootSiteEvent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeoutOrNull

/** Loads the referenced event through the account's existing cache and relay finders. */
class NsiteManifestLoader(
    private val cache: LocalCache,
    private val sources: RelaySubscriptionsCoordinator,
    private val account: UserFinderAccount,
    private val timeoutMs: Long = MANIFEST_FETCH_TIMEOUT_MS,
) {
    suspend fun load(target: NsiteTarget): Event {
        cache.consume(target.entity)
        val note = noteFor(target)
        recordAuthorHints(target)
        val noteSub = EventFinderQueryState(note, account)
        val userSub = note.author?.let { UserFinderQueryState(it, account) }

        return withTimeoutOrNull(timeoutMs) {
            note
                .flow()
                .metadata.stateFlow
                .onStart {
                    userSub?.let { sources.userFinder.subscribe(it) }
                    sources.eventFinder.subscribe(noteSub)
                }.onCompletion {
                    sources.eventFinder.unsubscribe(noteSub)
                    userSub?.let { sources.userFinder.unsubscribe(it) }
                }.mapNotNull { it.note.event }
                .first()
        } ?: throw NsiteFetchException()
    }

    private fun noteFor(target: NsiteTarget): Note =
        when (val entity = target.entity) {
            is NPub -> cache.getOrCreateAddressableNote(Address(RootSiteEvent.KIND, entity.hex, ""))
            is NProfile -> {
                val address = Address(RootSiteEvent.KIND, entity.hex, "")
                entity.relay.forEach { cache.relayHints.addAddress(address.toValue(), it) }
                cache.getOrCreateAddressableNote(address)
            }
            is NAddress -> cache.getOrCreateAddressableNote(entity.address())
            is NEvent -> cache.getOrCreateNote(entity.hex)
            is NNote -> cache.getOrCreateNote(entity.hex)
            else -> throw NsiteFetchException()
        }

    /** Address finders use address/event hints; author discovery also needs the pubkey hints. */
    private fun recordAuthorHints(target: NsiteTarget) {
        when (val entity = target.entity) {
            is NAddress -> entity.relay.forEach { cache.relayHints.addKey(entity.author, it) }
            is NEvent ->
                entity.author?.let { author ->
                    entity.relay.forEach { cache.relayHints.addKey(author, it) }
                }
            else -> Unit
        }
    }

    private companion object {
        const val MANIFEST_FETCH_TIMEOUT_MS = 30_000L
    }
}

class NsiteFetchException : Exception("The requested nSite manifest event could not be retrieved")
