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

import com.vitorpamplona.quartz.nip01Core.signers.NostrSignerSync
import com.vitorpamplona.quartz.nip19Bech32.entities.NAddress
import com.vitorpamplona.quartz.nip19Bech32.entities.NEvent
import com.vitorpamplona.quartz.nip19Bech32.entities.NPub
import com.vitorpamplona.quartz.nip5aStaticWebsites.NamedSiteEvent
import com.vitorpamplona.quartz.nip5aStaticWebsites.RootSiteEvent
import com.vitorpamplona.quartz.nip5aStaticWebsites.tags.PathTag
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class NsiteCoordinatorTest {
    private val signer = NostrSignerSync()

    @Test
    fun signedRootAndNamedManifestsLaunch() =
        runTest {
            val root = root(title = "Root site")
            val rootOutcome = NsiteCoordinator { root }.resolve(target(NPub.create(signer.pubKey)))
            assertEquals(root, assertIs<NsiteOutcome.Success>(rootOutcome).event)
            assertEquals("Root site", assertIs<NsiteOutcome.Success>(rootOutcome).launch.title)

            val rootAddressTarget = target(NAddress.create(RootSiteEvent.KIND, signer.pubKey, "", null))
            val rootAddressOutcome = NsiteCoordinator { root }.resolve(rootAddressTarget)
            assertEquals(root, assertIs<NsiteOutcome.Success>(rootAddressOutcome).event)

            val named = named(title = "Named site")
            val namedTarget = target(NAddress.create(NamedSiteEvent.KIND, signer.pubKey, "blog", null))
            val namedOutcome = NsiteCoordinator { named }.resolve(namedTarget)
            assertEquals(named, assertIs<NsiteOutcome.Success>(namedOutcome).event)
            assertEquals("Named site", assertIs<NsiteOutcome.Success>(namedOutcome).launch.title)
        }

    @Test
    fun eventHintsMustMatchTheReturnedManifest() =
        runTest {
            val root = root()
            val eventTarget = target(NEvent.create(root.id, signer.pubKey, RootSiteEvent.KIND, null))
            assertIs<NsiteOutcome.Success>(NsiteCoordinator { root }.resolve(eventTarget))

            val wrongAuthor = target(NEvent.create(root.id, NostrSignerSync().pubKey, RootSiteEvent.KIND, null))
            assertEquals(NsiteFailure.WRONG_EVENT, assertIs<NsiteOutcome.Failure>(NsiteCoordinator { root }.resolve(wrongAuthor)).reason)

            val wrongKind = target(NEvent.create(root.id, signer.pubKey, NamedSiteEvent.KIND, null))
            assertEquals(NsiteFailure.WRONG_EVENT, assertIs<NsiteOutcome.Failure>(NsiteCoordinator { root }.resolve(wrongKind)).reason)

            val wrongId = target(NEvent.create("00".repeat(32), signer.pubKey, RootSiteEvent.KIND, null))
            assertEquals(NsiteFailure.WRONG_EVENT, assertIs<NsiteOutcome.Failure>(NsiteCoordinator { root }.resolve(wrongId)).reason)
        }

    @Test
    fun wrongAuthorAndWrongNamedIdentifierFailBeforeLaunchValidation() =
        runTest {
            val other = NostrSignerSync().sign<RootSiteEvent>(RootSiteEvent.build(paths()))
            val rootOutcome = NsiteCoordinator { other }.resolve(target(NPub.create(signer.pubKey)))
            assertEquals(NsiteFailure.WRONG_EVENT, assertIs<NsiteOutcome.Failure>(rootOutcome).reason)

            val namedTarget = target(NAddress.create(NamedSiteEvent.KIND, signer.pubKey, "other", null))
            val namedOutcome = NsiteCoordinator { named() }.resolve(namedTarget)
            assertEquals(NsiteFailure.WRONG_EVENT, assertIs<NsiteOutcome.Failure>(namedOutcome).reason)

            val rootAddressTarget = target(NAddress.create(RootSiteEvent.KIND, signer.pubKey, "", null))
            val rootAddressOutcome = NsiteCoordinator { named() }.resolve(rootAddressTarget)
            assertEquals(NsiteFailure.WRONG_EVENT, assertIs<NsiteOutcome.Failure>(rootAddressOutcome).reason)
        }

    @Test
    fun forgedSignaturesAndInvalidSiteContentsFailClosed() =
        runTest {
            val signed = root()
            val forged = RootSiteEvent(signed.id, signed.pubKey, signed.createdAt, signed.tags, "changed", signed.sig)
            val target = target(NPub.create(signer.pubKey))
            assertEquals(NsiteFailure.INVALID_SITE, assertIs<NsiteOutcome.Failure>(NsiteCoordinator { forged }.resolve(target)).reason)

            val noPaths = signer.sign<RootSiteEvent>(RootSiteEvent.build(emptyList()))
            assertEquals(NsiteFailure.INVALID_SITE, assertIs<NsiteOutcome.Failure>(NsiteCoordinator { noPaths }.resolve(target)).reason)
        }

    @Test
    fun retrievalFailuresAreNotMisreportedAsInvalidSites() =
        runTest {
            val target = target(NPub.create(signer.pubKey))
            val failed = NsiteCoordinator { throw NsiteFetchException() }.resolve(target)
            assertEquals(NsiteFailure.FETCH_FAILED, assertIs<NsiteOutcome.Failure>(failed).reason)

            assertFailsWith<CancellationException> {
                NsiteCoordinator { throw CancellationException("cancelled") }.resolve(target)
            }
        }

    private fun target(reference: String): NsiteTarget = assertIs<NsiteTargetParseResult.Parsed>(NsiteTarget.parse(reference)).target

    private fun root(
        title: String = "Site",
        paths: List<PathTag> = paths(),
    ): RootSiteEvent = signer.sign(RootSiteEvent.build(paths, title = title, createdAt = 20))

    private fun named(title: String = "Site"): NamedSiteEvent = signer.sign(NamedSiteEvent.build("blog", paths(), title = title, createdAt = 20))

    private fun paths() = listOf(PathTag("/index.html", "ab".repeat(32)))
}
