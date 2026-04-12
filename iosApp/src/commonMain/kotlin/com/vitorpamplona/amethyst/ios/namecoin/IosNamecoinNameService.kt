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
package com.vitorpamplona.amethyst.ios.namecoin

import com.vitorpamplona.quartz.nip05DnsIdentifiers.namecoin.DEFAULT_ELECTRUMX_SERVERS
import com.vitorpamplona.quartz.nip05DnsIdentifiers.namecoin.ElectrumXClient
import com.vitorpamplona.quartz.nip05DnsIdentifiers.namecoin.NamecoinLookupCache
import com.vitorpamplona.quartz.nip05DnsIdentifiers.namecoin.NamecoinNameResolver
import com.vitorpamplona.quartz.nip05DnsIdentifiers.namecoin.NamecoinNostrResult
import com.vitorpamplona.quartz.nip05DnsIdentifiers.namecoin.NamecoinResolveOutcome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class NamecoinResolveState {
    data object Idle : NamecoinResolveState()

    data object Resolving : NamecoinResolveState()

    data class Resolved(
        val result: NamecoinNostrResult,
    ) : NamecoinResolveState()

    data class Error(
        val message: String,
    ) : NamecoinResolveState()
}

class IosNamecoinNameService {
    private val client = ElectrumXClient()
    private val cache = NamecoinLookupCache()

    private val resolver =
        NamecoinNameResolver(
            electrumxClient = client,
            serverListProvider = { DEFAULT_ELECTRUMX_SERVERS },
        )

    private val _resolveState = MutableStateFlow<NamecoinResolveState>(NamecoinResolveState.Idle)
    val resolveState: StateFlow<NamecoinResolveState> = _resolveState

    suspend fun resolve(identifier: String): NamecoinNostrResult? {
        val cached = cache.get(identifier)
        if (cached != null) {
            val cachedResult = cached.result
            if (cachedResult != null) {
                _resolveState.value = NamecoinResolveState.Resolved(cachedResult)
            }
            return cachedResult
        }

        _resolveState.value = NamecoinResolveState.Resolving

        val outcome = resolver.resolveDetailed(identifier)

        return when (outcome) {
            is NamecoinResolveOutcome.Success -> {
                cache.put(identifier, outcome.result)
                _resolveState.value = NamecoinResolveState.Resolved(outcome.result)
                outcome.result
            }

            is NamecoinResolveOutcome.NameNotFound -> {
                cache.put(identifier, null)
                _resolveState.value = NamecoinResolveState.Error("Name not found: $identifier")
                null
            }

            is NamecoinResolveOutcome.NoNostrField -> {
                cache.put(identifier, null)
                _resolveState.value = NamecoinResolveState.Error("No Nostr field in name value")
                null
            }

            is NamecoinResolveOutcome.ServersUnreachable -> {
                _resolveState.value = NamecoinResolveState.Error("ElectrumX servers unreachable")
                null
            }

            is NamecoinResolveOutcome.InvalidIdentifier -> {
                _resolveState.value = NamecoinResolveState.Error("Invalid identifier: $identifier")
                null
            }

            is NamecoinResolveOutcome.Timeout -> {
                _resolveState.value = NamecoinResolveState.Error("Resolution timed out")
                null
            }
        }
    }

    fun isNamecoinIdentifier(identifier: String): Boolean = NamecoinNameResolver.isNamecoinIdentifier(identifier)

    fun resetState() {
        _resolveState.value = NamecoinResolveState.Idle
    }
}
