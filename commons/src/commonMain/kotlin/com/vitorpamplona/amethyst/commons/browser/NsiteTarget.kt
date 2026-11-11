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

import com.vitorpamplona.quartz.nip01Core.core.Event
import com.vitorpamplona.quartz.nip19Bech32.Nip19Parser
import com.vitorpamplona.quartz.nip19Bech32.entities.Entity
import com.vitorpamplona.quartz.nip19Bech32.entities.NAddress
import com.vitorpamplona.quartz.nip19Bech32.entities.NEvent
import com.vitorpamplona.quartz.nip19Bech32.entities.NNote
import com.vitorpamplona.quartz.nip19Bech32.entities.NProfile
import com.vitorpamplona.quartz.nip19Bech32.entities.NPub
import com.vitorpamplona.quartz.nip5aStaticWebsites.NamedSiteEvent
import com.vitorpamplona.quartz.nip5aStaticWebsites.RootSiteEvent

sealed interface NsiteTargetParseResult {
    data class Parsed(
        val target: NsiteTarget,
    ) : NsiteTargetParseResult

    data object Invalid : NsiteTargetParseResult

    data object NotNsite : NsiteTargetParseResult
}

/** A NIP-19 reference to a live NIP-5A root or named manifest, never a DNS name. */
class NsiteTarget private constructor(
    val original: String,
    val entity: Entity,
) {
    /** Checks every locator hint as well as the signed event's actual NIP-5A type. */
    fun matches(event: Event): Boolean =
        when (val ref = entity) {
            is NPub -> event is RootSiteEvent && event.pubKey == ref.hex
            is NProfile -> event is RootSiteEvent && event.pubKey == ref.hex
            is NAddress ->
                event.pubKey == ref.author &&
                    when (ref.kind) {
                        RootSiteEvent.KIND -> ref.dTag.isEmpty() && event is RootSiteEvent
                        NamedSiteEvent.KIND -> ref.dTag.isNotEmpty() && event is NamedSiteEvent && event.identifier() == ref.dTag
                        else -> false
                    }
            is NEvent ->
                event.id == ref.hex &&
                    (ref.author == null || event.pubKey == ref.author) &&
                    (ref.kind == null || event.kind == ref.kind) &&
                    (event is RootSiteEvent || event is NamedSiteEvent)
            is NNote -> event.id == ref.hex && (event is RootSiteEvent || event is NamedSiteEvent)
            else -> false
        }

    companion object {
        private const val MAX_IDENTIFIER_LENGTH = 5000
        private val SUPPORTED_PREFIXES = listOf("npub1", "nprofile1", "nevent1", "note1", "naddr1")

        fun parse(input: String): NsiteTargetParseResult {
            val original = input.trim()
            val bare = if (original.startsWith(NOSTR_PREFIX, ignoreCase = true)) original.substring(NOSTR_PREFIX.length) else original
            if (SUPPORTED_PREFIXES.none { bare.startsWith(it, ignoreCase = true) }) return NsiteTargetParseResult.NotNsite
            if (bare.length > MAX_IDENTIFIER_LENGTH) return NsiteTargetParseResult.Invalid

            val route = runCatching { Nip19Parser.uriToRoute(bare) }.getOrNull()
            if (route == null || route.additionalChars != null || !bare.equals(route.nip19raw, ignoreCase = true)) {
                return NsiteTargetParseResult.Invalid
            }

            val supported =
                when (val entity = route.entity) {
                    is NPub, is NProfile, is NNote -> true
                    is NEvent -> entity.kind == null || entity.kind == RootSiteEvent.KIND || entity.kind == NamedSiteEvent.KIND
                    is NAddress ->
                        when (entity.kind) {
                            RootSiteEvent.KIND -> entity.dTag.isEmpty()
                            NamedSiteEvent.KIND -> entity.dTag.isNotEmpty()
                            else -> false
                        }
                    else -> false
                }
            return if (supported) NsiteTargetParseResult.Parsed(NsiteTarget(original, route.entity)) else NsiteTargetParseResult.Invalid
        }

        private const val NOSTR_PREFIX = "nostr:"
    }
}
