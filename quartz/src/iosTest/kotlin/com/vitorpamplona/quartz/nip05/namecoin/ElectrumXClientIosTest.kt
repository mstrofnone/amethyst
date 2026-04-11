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
package com.vitorpamplona.quartz.nip05.namecoin

import com.vitorpamplona.quartz.nip05DnsIdentifiers.namecoin.DEFAULT_ELECTRUMX_SERVERS
import com.vitorpamplona.quartz.nip05DnsIdentifiers.namecoin.ElectrumXClient
import com.vitorpamplona.quartz.nip05DnsIdentifiers.namecoin.NamecoinNameResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * Live integration test for the iOS ElectrumXClient.
 *
 * Connects to real ElectrumX servers over TLS via Network.framework
 * and resolves the well-known test name d/testls on the Namecoin blockchain.
 *
 * Requires network access on the iOS simulator.
 */
class ElectrumXClientIosTest {
    @Test
    fun resolveTestlsViaDirect() =
        runTest {
            val client = ElectrumXClient()
            val result = client.nameShowWithFallback("d/testls", DEFAULT_ELECTRUMX_SERVERS)
            assertNotNull(result, "d/testls should resolve via ElectrumX")
            assertEquals("d/testls", result.name)
            assertTrue(result.value.contains("nostr"), "d/testls value should contain nostr field")
            println("iOS ElectrumXClient: d/testls resolved — value=${result.value.take(100)}")
        }

    @Test
    fun resolveTestlsViaNamecoinNameResolver() =
        runTest(timeout = 60.seconds) {
            // NamecoinNameResolver.resolve() uses withTimeoutOrNull internally,
            // which requires real time dispatching. We wrap in withContext(IO)
            // so the timeout runs on a real dispatcher, not the test scheduler.
            val client = ElectrumXClient()
            val resolver = NamecoinNameResolver(electrumxClient = client)
            val result =
                withContext(Dispatchers.IO) {
                    resolver.resolve("testls.bit")
                }
            assertNotNull(result, "testls.bit should resolve to a Nostr pubkey")
            assertEquals(64, result.pubkey.length, "pubkey should be 64 hex chars")
            assertTrue(result.pubkey.matches(Regex("^[0-9a-f]{64}$")), "pubkey should be lowercase hex")
            println("iOS NamecoinNameResolver: testls.bit → ${result.pubkey}")
        }

    @Test
    fun resolveNamedUserAtTestls() =
        runTest(timeout = 60.seconds) {
            val client = ElectrumXClient()
            val resolver = NamecoinNameResolver(electrumxClient = client)
            // d/testls has "names": {"m": "6cdebcca..."} — resolve m@testls.bit
            val result =
                withContext(Dispatchers.IO) {
                    resolver.resolve("m@testls.bit")
                }
            assertNotNull(result, "m@testls.bit should resolve")
            assertEquals("6cdebccabda1dfa058ab85352a79509b592b2bdfa0370325e28ec1cb4f18667d", result.pubkey)
            println("iOS NamecoinNameResolver: m@testls.bit → ${result.pubkey}")
        }
}
