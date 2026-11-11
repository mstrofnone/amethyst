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

import com.vitorpamplona.amethyst.commons.napplet.NappletLaunch
import com.vitorpamplona.quartz.nip01Core.core.Event
import kotlinx.coroutines.CancellationException

enum class NsiteFailure { INVALID_IDENTIFIER, FETCH_FAILED, TIMEOUT, WRONG_EVENT, INVALID_SITE }

sealed interface NsiteOutcome {
    val original: String

    data class Success(
        override val original: String,
        val event: Event,
        val launch: NappletLaunch,
    ) : NsiteOutcome

    data class Failure(
        override val original: String,
        val reason: NsiteFailure,
    ) : NsiteOutcome
}

/** Retrieves and validates exactly the manifest identified by a NIP-19 target. */
class NsiteCoordinator(
    private val loadEvent: suspend (NsiteTarget) -> Event,
) {
    suspend fun resolve(target: NsiteTarget): NsiteOutcome {
        val event =
            try {
                loadEvent(target)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                return NsiteOutcome.Failure(target.original, NsiteFailure.FETCH_FAILED)
            }

        if (!target.matches(event)) return NsiteOutcome.Failure(target.original, NsiteFailure.WRONG_EVENT)
        val launch = NappletLaunch.fromSite(event).getOrNull() ?: return NsiteOutcome.Failure(target.original, NsiteFailure.INVALID_SITE)
        return NsiteOutcome.Success(target.original, event, launch)
    }
}
