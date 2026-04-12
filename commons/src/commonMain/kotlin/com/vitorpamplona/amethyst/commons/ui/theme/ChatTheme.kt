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
package com.vitorpamplona.amethyst.commons.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

// Chat-prefixed aliases for commons UI components that use these names.
// The canonical definitions now live in Shape.kt.
val ChatRowColSpacing5dp = RowColSpacing5dp
val ChatHalfHalfVertPadding = HalfHalfVertPadding
val ChatStdHorzSpacer = StdHorzSpacer
val ChatStdPadding = StdPadding
val ChatReactionRowHeight = ReactionRowHeightChat
val ChatSize20dp = Size20dp
val ChatSize34dp = Size34dp

// Chat-prefixed aliases for PascalCase originals in Shape.kt
val ChatAuthorBox = chatAuthorBox
val ChatAuthorImage = chatAuthorImage
val MessageBubbleLimits = messageBubbleLimits

// Chat color extensions for ColorScheme
val ColorScheme.chatBubbleBackground: Color
    get() = if (isLight) onSurface.copy(alpha = 0.08f) else onSurface.copy(alpha = 0.12f)

val ColorScheme.chatBubbleDraftBackground: Color
    get() = onSurface.copy(alpha = 0.15f)

val ColorScheme.chatBubbleMeBackground: Color
    get() = primary.copy(alpha = 0.32f)

val ColorScheme.chatPlaceholderText: Color
    get() = onSurface.copy(alpha = 0.42f)
