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
package com.vitorpamplona.amethyst.commons.browser.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitorpamplona.amethyst.commons.browser.NsiteFailure
import com.vitorpamplona.amethyst.commons.browser.NsiteOutcome
import com.vitorpamplona.amethyst.commons.resources.Res
import com.vitorpamplona.amethyst.commons.resources.nsite_event_cancel
import com.vitorpamplona.amethyst.commons.resources.nsite_event_failure_fetch
import com.vitorpamplona.amethyst.commons.resources.nsite_event_failure_invalid_identifier
import com.vitorpamplona.amethyst.commons.resources.nsite_event_failure_invalid_site
import com.vitorpamplona.amethyst.commons.resources.nsite_event_failure_timeout
import com.vitorpamplona.amethyst.commons.resources.nsite_event_failure_title
import com.vitorpamplona.amethyst.commons.resources.nsite_event_failure_wrong_event
import com.vitorpamplona.amethyst.commons.resources.nsite_event_resolving_message
import com.vitorpamplona.amethyst.commons.resources.nsite_event_resolving_title
import com.vitorpamplona.amethyst.commons.resources.nsite_event_retry
import com.vitorpamplona.amethyst.commons.ui.stringRes
import org.jetbrains.compose.resources.StringResource

/** Loading and failure UI for a NIP-19 manifest request on Android and desktop. */
@Composable
fun NsiteNavigationDialog(
    resolving: String?,
    outcome: NsiteOutcome?,
    onDismiss: () -> Unit,
    onRetry: (String) -> Unit,
) {
    if (resolving != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringRes(Res.string.nsite_event_resolving_title)) },
            text = {
                Column {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text(stringRes(Res.string.nsite_event_resolving_message))
                    OriginalTarget(resolving)
                }
            },
            confirmButton = { TextButton(onClick = onDismiss) { Text(stringRes(Res.string.nsite_event_cancel)) } },
        )
    }

    if (outcome is NsiteOutcome.Failure) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringRes(Res.string.nsite_event_failure_title)) },
            text = {
                Column {
                    Text(stringRes(outcome.reason.messageResource()))
                    OriginalTarget(outcome.original)
                }
            },
            confirmButton = { TextButton(onClick = { onRetry(outcome.original) }) { Text(stringRes(Res.string.nsite_event_retry)) } },
            dismissButton = { TextButton(onClick = onDismiss) { Text(stringRes(Res.string.nsite_event_cancel)) } },
        )
    }
}

@Composable
private fun OriginalTarget(target: String) {
    Spacer(Modifier.height(12.dp))
    Text(target, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

private fun NsiteFailure.messageResource(): StringResource =
    when (this) {
        NsiteFailure.INVALID_IDENTIFIER -> Res.string.nsite_event_failure_invalid_identifier
        NsiteFailure.FETCH_FAILED -> Res.string.nsite_event_failure_fetch
        NsiteFailure.TIMEOUT -> Res.string.nsite_event_failure_timeout
        NsiteFailure.WRONG_EVENT -> Res.string.nsite_event_failure_wrong_event
        NsiteFailure.INVALID_SITE -> Res.string.nsite_event_failure_invalid_site
    }
