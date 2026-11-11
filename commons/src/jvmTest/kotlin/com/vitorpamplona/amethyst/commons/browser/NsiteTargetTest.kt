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

import com.vitorpamplona.quartz.nip01Core.relay.normalizer.RelayUrlNormalizer
import com.vitorpamplona.quartz.nip19Bech32.entities.NAddress
import com.vitorpamplona.quartz.nip19Bech32.entities.NEvent
import com.vitorpamplona.quartz.nip19Bech32.entities.NNote
import com.vitorpamplona.quartz.nip19Bech32.entities.NProfile
import com.vitorpamplona.quartz.nip19Bech32.entities.NPub
import com.vitorpamplona.quartz.nip5aStaticWebsites.NamedSiteEvent
import com.vitorpamplona.quartz.nip5aStaticWebsites.RootSiteEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class NsiteTargetTest {
    private val author = "ab".repeat(32)
    private val id = "cd".repeat(32)
    private val hint = RelayUrlNormalizer.normalizeOrNull("wss://relay.example.com")!!
    private val rootNaddr =
        "naddr1qvzqqqpmrqpzqfngzhsvjggdlgeycm96x4emzjlwf8dyyzdfg4hefp89zpkdgz99qythwumn8ghj7un9d3shjtn90psk6urvv5hxxmmdqqqqk9fgz7"

    @Test
    fun authorIdentifiersSelectTheRootManifest() {
        val npub = NPub.create(author)
        val fromNpub = assertIs<NsiteTargetParseResult.Parsed>(NsiteTarget.parse(npub)).target
        assertEquals(author, assertIs<NPub>(fromNpub.entity).hex)
        assertEquals(npub, fromNpub.original)

        val nprofile = NProfile.create(author, listOf(hint))
        val fromNprofile = assertIs<NsiteTargetParseResult.Parsed>(NsiteTarget.parse("  NoStR:$nprofile  ")).target
        assertEquals("NoStR:$nprofile", fromNprofile.original)
        assertEquals(listOf(hint), assertIs<NProfile>(fromNprofile.entity).relay)
    }

    @Test
    fun addressesSelectNamedManifests() {
        val named = assertIs<NsiteTargetParseResult.Parsed>(NsiteTarget.parse(NAddress.create(NamedSiteEvent.KIND, author, "blog", hint))).target
        assertEquals("blog", assertIs<NAddress>(named.entity).dTag)
        assertEquals(listOf(hint), assertIs<NAddress>(named.entity).relay)
    }

    @Test
    fun rootAddressSelectsTheCurrentRootManifest() {
        val root = assertIs<NsiteTargetParseResult.Parsed>(NsiteTarget.parse(rootNaddr)).target
        val address = assertIs<NAddress>(root.entity)

        assertEquals("266815e0c9210dfa324c6cba3573b14bee49da4209a9456f9484e5106cd408a5", address.author)
        assertEquals(RootSiteEvent.KIND, address.kind)
        assertEquals("", address.dTag)
        assertEquals(listOf(hint), address.relay)
    }

    @Test
    fun eventIdentifiersSelectSpecificManifestEvents() {
        val nevent = NEvent.create(id, author, RootSiteEvent.KIND, hint)
        val fromNevent = assertIs<NsiteTargetParseResult.Parsed>(NsiteTarget.parse(nevent)).target
        assertEquals(id, assertIs<NEvent>(fromNevent.entity).hex)
        assertEquals(author, assertIs<NEvent>(fromNevent.entity).author)
        assertEquals(listOf(hint), assertIs<NEvent>(fromNevent.entity).relay)

        val note = NNote.create(id)
        val fromNote = assertIs<NsiteTargetParseResult.Parsed>(NsiteTarget.parse(note)).target
        assertEquals(id, assertIs<NNote>(fromNote.entity).hex)
    }

    @Test
    fun unrelatedInputRemainsOrdinaryBrowserInput() {
        listOf("https://example.bit/", "example.com", "search nSites", "", "nsec1private").forEach {
            assertSame(NsiteTargetParseResult.NotNsite, NsiteTarget.parse(it), it)
        }
    }

    @Test
    fun malformedAndNonManifestIdentifiersAreRejected() {
        val invalid =
            listOf(
                "nostr:npub1bad",
                NPub.create(author) + "trailing",
                NAddress.create(30023, author, "article", hint),
                NAddress.create(30023, author, "", hint),
                NAddress.create(RootSiteEvent.KIND, author, "not-root", hint),
                NAddress.create(NamedSiteEvent.KIND, author, "", hint),
                NEvent.create(id, author, 5128, hint),
                NEvent.create(id, author, 1, hint),
            )
        invalid.forEach { assertSame(NsiteTargetParseResult.Invalid, NsiteTarget.parse(it), it) }
    }
}
