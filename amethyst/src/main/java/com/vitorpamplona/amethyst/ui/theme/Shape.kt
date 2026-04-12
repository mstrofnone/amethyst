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
package com.vitorpamplona.amethyst.ui.theme

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Shapes
import androidx.compose.material3.ripple
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitorpamplona.amethyst.commons.ui.theme.Font17SP
import com.vitorpamplona.amethyst.ui.navigation.topbars.TopBarSize

val Shapes =
    Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp),
    )

val RippleRadius45dp = 45.dp // Ripple should be +10.dp over the component size

val BottomTopHeight = Modifier.height(50.dp)
val TabRowHeight = Modifier

val SmallestBorder = RoundedCornerShape(5.dp)
val SmallBorder = RoundedCornerShape(7.dp)
val SmallishBorder = RoundedCornerShape(9.dp)
val QuoteBorder = RoundedCornerShape(15.dp)

val ButtonBorder = RoundedCornerShape(20.dp)
val LeftHalfCircleButtonBorder = ButtonBorder.copy(topEnd = CornerSize(0f), bottomEnd = CornerSize(0f))
val EditFieldBorder = RoundedCornerShape(25.dp)

val ChatBubbleShapeMe = RoundedCornerShape(15.dp, 15.dp, 3.dp, 15.dp)
val ChatBubbleShapeThem = RoundedCornerShape(3.dp, 15.dp, 15.dp, 15.dp)

val StdButtonSizeModifier = Modifier.size(19.dp)

val HalfVertSpacer = Modifier.height(2.dp)

val MinHorzSpacer = Modifier.width(1.dp)

val HalfHorzSpacer = Modifier.width(3.dp)

val StdHorzSpacer = Modifier.width(5.dp)
val StdVertSpacer = Modifier.height(5.dp)

val DoubleHorzSpacer = Modifier.width(10.dp)
val DoubleVertSpacer = Modifier.height(10.dp)

val Height100Modifier = Modifier.height(100.dp)

val HalfDoubleVertSpacer = Modifier.height(7.dp)

val Size0dp = 0.dp
val Size2dp = 2.dp
val Size3dp = 3.dp
val Size5dp = 5.dp
val Size6dp = 6.dp
val Size8dp = 8.dp
val Size10dp = 10.dp
val Size12dp = 12.dp
val Size13dp = 13.dp
val Size14dp = 14.dp
val Size15dp = 15.dp
val Size16dp = 16.dp
val Size17dp = 17.dp
val Size18dp = 18.dp
val Size19dp = 19.dp
val Size20dp = 20.dp
val Size22dp = 22.dp
val Size23dp = 23.dp
val Size24dp = 24.dp
val Size25dp = 25.dp
val Size30dp = 30.dp
val Size34dp = 34.dp
val Size35dp = 35.dp
val Size40dp = 40.dp
val Size50dp = 50.dp
val Size55dp = 55.dp
val Size75dp = 75.dp
val Size100dp = 100.dp
val Size110dp = 110.dp
val Size165dp = 165.dp

val StdEndPadding = Modifier.padding(end = 10.dp)
val HalfEndPadding = Modifier.padding(end = 5.dp)
val HalfStartPadding = Modifier.padding(start = 5.dp)
val StdStartPadding = Modifier.padding(start = 10.dp)
val StdTopPadding = Modifier.padding(top = 10.dp)
val HalfTopPadding = Modifier.padding(top = 5.dp)
val HalfHalfTopPadding = Modifier.padding(top = 3.dp)

val HalfHalfVertPadding = Modifier.padding(vertical = 3.dp)
val HalfHalfHorzModifier = Modifier.padding(horizontal = 3.dp)

val HalfPadding = Modifier.padding(5.dp)
val StdPadding = Modifier.padding(10.dp)
val BigPadding = Modifier.padding(15.dp)

val RowColSpacing = Arrangement.spacedBy(3.dp)
val RowColSpacing5dp = Arrangement.spacedBy(5.dp)
val RowColSpacing10dp = Arrangement.spacedBy(10.dp)

val HalfHorzPadding = Modifier.padding(horizontal = 5.dp)
val HalfVertPadding = Modifier.padding(vertical = 5.dp)

val HorzPadding = Modifier.padding(horizontal = 10.dp)
val VertPadding = Modifier.padding(vertical = 10.dp)

val HorzHalfVertPadding = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)

val DoubleHorzPadding = Modifier.padding(horizontal = 20.dp)
val DoubleVertPadding = Modifier.padding(vertical = 20.dp)

val MaxWidthWithHorzPadding = Modifier.fillMaxWidth().padding(horizontal = 10.dp)

val Size5Modifier = Modifier.size(5.dp)
val Size10Modifier = Modifier.size(10.dp)
val Size14Modifier = Modifier.size(14.dp)
val Size15Modifier = Modifier.size(15.dp)
val Size16Modifier = Modifier.size(16.dp)
val Size17Modifier = Modifier.size(17.dp)
val Size18Modifier = Modifier.size(18.dp)
val Size19Modifier = Modifier.size(19.dp)
val Size20Modifier = Modifier.size(20.dp)
val Size22Modifier = Modifier.size(22.dp)
val Size24Modifier = Modifier.size(24.dp)
val Size25Modifier = Modifier.size(25.dp)
val Size26Modifier = Modifier.size(26.dp)
val Size28Modifier = Modifier.size(28.dp)
val Size30Modifier = Modifier.size(30.dp)
val Size35Modifier = Modifier.size(35.dp)
val Size39Modifier = Modifier.size(39.dp)
val Size40Modifier = Modifier.size(40.dp)
val Size50Modifier = Modifier.size(50.dp)
val Size55Modifier = Modifier.size(55.dp)
val Size75Modifier = Modifier.size(75.dp)

val TinyBorders = Modifier.padding(2.dp)
val NoSoTinyBorders = Modifier.padding(start = 5.dp, end = 5.dp, top = 2.dp, bottom = 2.dp)
val ReactionRowZapraiserWithPadding = Modifier.defaultMinSize(minHeight = 4.dp).padding(start = Size75dp).fillMaxWidth()
val ReactionRowZapraiser = Modifier.defaultMinSize(minHeight = 4.dp).fillMaxWidth()

val ReactionRowExpandButton = Modifier.width(65.dp).padding(start = 31.dp)

val WidthAuthorPictureModifier = Modifier.width(55.dp)
val WidthAuthorPictureModifierWithPadding = Modifier.width(65.dp)

val VideoReactionColumnPadding = Modifier.padding(bottom = 75.dp)

val DividerThickness = 0.25.dp

val ReactionRowHeight = Modifier.padding(vertical = 7.dp).heightIn(min = 24.dp)
val ReactionRowHeightWithPadding = Modifier.padding(vertical = 6.dp).heightIn(min = 24.dp).padding(horizontal = 10.dp)
val ReactionRowHeightChat = Modifier.height(20.dp)
val ReactionRowHeightChatMaxWidth = Modifier.height(25.dp).fillMaxWidth()
val UserNameRowHeight = Modifier.fillMaxWidth()
val UserNameMaxRowHeight = Modifier.fillMaxWidth()

val Height24dpModifier = Modifier.height(24.dp)
val Height4dpModifier = Modifier.height(4.dp)
val Height25Modifier = Modifier.height(Size25dp)

val Height24dpFilledModifier = Modifier.fillMaxWidth().height(24.dp)
val Height4dpFilledModifier = Modifier.fillMaxWidth().height(4.dp)

val AccountPictureModifier = Modifier.size(55.dp).clip(shape = CircleShape)
val HeaderPictureModifier = Modifier.size(34.dp).clip(shape = CircleShape)

val ShowMoreRelaysButtonIconButtonModifier = Modifier.size(15.dp)
val ShowMoreRelaysButtonIconModifier = Modifier.size(20.dp)
val ShowMoreRelaysButtonBoxModifer = Modifier.width(55.dp).height(17.dp)

val ChatBubbleMaxSizeModifier = Modifier.fillMaxWidth(0.85f)

val ModifierWidth3dp = Modifier.width(3.dp)

val NotificationIconModifier = Modifier.width(55.dp).padding(end = 5.dp)
val NotificationIconModifierSmaller = Modifier.width(55.dp).padding(end = 4.dp)

val ZapPictureCommentModifier = Modifier.height(35.dp).widthIn(min = 35.dp)
val ChatHeadlineBorders = StdPadding

val VolumeBottomIconSize = Modifier.size(60.dp).padding(5.dp)
val PinBottomIconSize = Modifier.size(60.dp).padding(5.dp)
val PlayIconSize = Modifier.size(110.dp).padding(10.dp)
val NIP05IconSize = Modifier.size(13.dp).padding(top = 1.dp, start = 1.dp, end = 1.dp)

val CashuCardBorders = Modifier.fillMaxWidth().padding(10.dp).clip(shape = QuoteBorder)

val EditFieldModifier =
    Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 5.dp).fillMaxWidth()
val EditFieldTrailingIconModifier = Modifier.padding(start = 5.dp, end = 0.dp)

val ZeroPadding = PaddingValues(0.dp)
val HalfFeedPadding = PaddingValues(5.dp)
val FeedPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
val ButtonPadding = PaddingValues(vertical = 6.dp, horizontal = 16.dp)

val ChatPaddingInnerQuoteModifier = Modifier
val ChatPaddingModifier =
    Modifier
        .fillMaxWidth(1f)
        .padding(
            start = 12.dp,
            end = 12.dp,
            top = 3.dp,
            bottom = 3.dp,
        )

val profileContentHeaderModifier =
    Modifier.fillMaxWidth().padding(top = 70.dp, start = Size25dp, end = Size25dp)
val bannerModifier = Modifier.fillMaxWidth().height(120.dp)
val drawerSpacing = Modifier.padding(top = Size10dp, start = Size25dp, end = Size25dp)

val IconRowTextModifier = Modifier.padding(start = 16.dp)
val IconRowModifier = Modifier.fillMaxWidth().padding(vertical = 15.dp, horizontal = 25.dp)

val Width16Space = Modifier.width(Size16dp)

val emptyLineItemModifier = Modifier.height(Size75dp).fillMaxWidth()

val imageHeaderBannerSize = Modifier.fillMaxWidth().height(150.dp)

val authorNotePictureForImageHeader = Modifier.size(75.dp).padding(10.dp)

val normalWithTopMarginNoteModifier =
    Modifier
        .fillMaxWidth()
        .padding(
            start = 12.dp,
            end = 12.dp,
            top = 10.dp,
        )

val MaxWidthWithHorzPadding = CommonsMaxWidthWithHorzPadding

val Size5Modifier = CommonsSize5Modifier
val Size10Modifier = CommonsSize10Modifier
val Size14Modifier = CommonsSize14Modifier
val Size15Modifier = CommonsSize15Modifier
val Size16Modifier = CommonsSize16Modifier
val Size17Modifier = CommonsSize17Modifier
val Size18Modifier = CommonsSize18Modifier
val Size19Modifier = CommonsSize19Modifier
val Size20Modifier = CommonsSize20Modifier
val Size22Modifier = CommonsSize22Modifier
val Size24Modifier = CommonsSize24Modifier
val Size25Modifier = CommonsSize25Modifier
val Size26Modifier = CommonsSize26Modifier
val Size28Modifier = CommonsSize28Modifier
val Size30Modifier = CommonsSize30Modifier
val Size35Modifier = CommonsSize35Modifier
val Size39Modifier = CommonsSize39Modifier
val Size40Modifier = CommonsSize40Modifier
val Size50Modifier = CommonsSize50Modifier
val Size55Modifier = CommonsSize55Modifier
val Size75Modifier = CommonsSize75Modifier

val TinyBorders = CommonsTinyBorders
val NoSoTinyBorders = CommonsNoSoTinyBorders
val ReactionRowZapraiserWithPadding = CommonsReactionRowZapraiserWithPadding
val ReactionRowZapraiser = CommonsReactionRowZapraiser

val ReactionRowExpandButton = CommonsReactionRowExpandButton

val WidthAuthorPictureModifier = CommonsWidthAuthorPictureModifier
val WidthAuthorPictureModifierWithPadding = CommonsWidthAuthorPictureModifierWithPadding

val VideoReactionColumnPadding = CommonsVideoReactionColumnPadding

val DividerThickness = CommonsDividerThickness

val ReactionRowHeight = CommonsReactionRowHeight
val ReactionRowHeightWithPadding = CommonsReactionRowHeightWithPadding
val ReactionRowHeightChat = CommonsReactionRowHeightChat
val ReactionRowHeightChatMaxWidth = CommonsReactionRowHeightChatMaxWidth
val UserNameRowHeight = CommonsUserNameRowHeight
val UserNameMaxRowHeight = CommonsUserNameMaxRowHeight

val Height24dpModifier = CommonsHeight24dpModifier
val Height4dpModifier = CommonsHeight4dpModifier
val Height25Modifier = CommonsHeight25Modifier

val Height24dpFilledModifier = CommonsHeight24dpFilledModifier
val Height4dpFilledModifier = CommonsHeight4dpFilledModifier

val AccountPictureModifier = CommonsAccountPictureModifier
val HeaderPictureModifier = CommonsHeaderPictureModifier

val ShowMoreRelaysButtonIconButtonModifier = CommonsShowMoreRelaysButtonIconButtonModifier
val ShowMoreRelaysButtonIconModifier = CommonsShowMoreRelaysButtonIconModifier
val ShowMoreRelaysButtonBoxModifer = CommonsShowMoreRelaysButtonBoxModifer

val ChatBubbleMaxSizeModifier = CommonsChatBubbleMaxSizeModifier

val ModifierWidth3dp = CommonsModifierWidth3dp

val NotificationIconModifier = CommonsNotificationIconModifier
val NotificationIconModifierSmaller = CommonsNotificationIconModifierSmaller

val ZapPictureCommentModifier = CommonsZapPictureCommentModifier
val ChatHeadlineBorders = CommonsChatHeadlineBorders

val VolumeBottomIconSize = CommonsVolumeBottomIconSize
val PinBottomIconSize = CommonsPinBottomIconSize
val PlayIconSize = CommonsPlayIconSize
val NIP05IconSize = CommonsNIP05IconSize

val CashuCardBorders = CommonsCashuCardBorders

val EditFieldModifier = CommonsEditFieldModifier
val EditFieldTrailingIconModifier = CommonsEditFieldTrailingIconModifier

val ZeroPadding = CommonsZeroPadding
val HalfFeedPadding = CommonsHalfFeedPadding
val FeedPadding = CommonsFeedPadding
val ButtonPadding = CommonsButtonPadding

val ChatPaddingInnerQuoteModifier = CommonsChatPaddingInnerQuoteModifier
val ChatPaddingModifier = CommonsChatPaddingModifier

val profileContentHeaderModifier = commonsProfileContentHeaderModifier
val bannerModifier = commonsBannerModifier
val drawerSpacing = commonsDrawerSpacing

val IconRowTextModifier = CommonsIconRowTextModifier
val IconRowModifier = CommonsIconRowModifier

val Width16Space = CommonsWidth16Space

val emptyLineItemModifier = commonsEmptyLineItemModifier

val imageHeaderBannerSize = commonsImageHeaderBannerSize

val authorNotePictureForImageHeader = commonsAuthorNotePictureForImageHeader

val normalWithTopMarginNoteModifier = commonsNormalWithTopMarginNoteModifier

val boostedNoteModifier = commonsBoostedNoteModifier

val liveStreamTag = commonsLiveStreamTag

val chatAuthorBox = commonsChatAuthorBox
val chatAuthorImage = commonsChatAuthorImage
val AuthorInfoVideoFeed = CommonsAuthorInfoVideoFeed

val messageDetailsModifier = commonsMessageDetailsModifier
val messageBubbleLimits = commonsMessageBubbleLimits

val inlinePlaceholder = commonsInlinePlaceholder

val IncognitoIconModifier = CommonsIncognitoIconModifier
val IncognitoIconButtonModifier = CommonsIncognitoIconButtonModifier

val hashVerifierMark = commonsHashVerifierMark

val noteComposeRelayBox = commonsNoteComposeRelayBox

val previewCardImageModifier = commonsPreviewCardImageModifier

val reactionBox = commonsReactionBox

val ripple24dp = commonsRipple24dp

val defaultTweenDuration = commonsDefaultTweenDuration
val defaultTweenFloatSpec = commonsDefaultTweenFloatSpec
val defaultTweenIntOffsetSpec = commonsDefaultTweenIntOffsetSpec

val StreamingHeaderModifier = CommonsStreamingHeaderModifier

val PostKeyboard = CommonsPostKeyboard

val SettingsCategoryFirstModifier = CommonsSettingsCategoryFirstModifier
val SettingsCategorySpacingModifier = CommonsSettingsCategorySpacingModifier

val SettingsCategoryFirstWithHorzBorderModifier = CommonsSettingsCategoryFirstWithHorzBorderModifier
val SettingsCategorySpacingWithHorzBorderModifier = CommonsSettingsCategorySpacingWithHorzBorderModifier

val SquaredQuoteBorderModifier = CommonsSquaredQuoteBorderModifier
val FillWidthQuoteBorderModifier = CommonsFillWidthQuoteBorderModifier

val MediumRelayIconModifier = CommonsMediumRelayIconModifier

val LargeRelayIconModifier = CommonsLargeRelayIconModifier

val FollowSetImageModifier = CommonsFollowSetImageModifier

val SimpleImage75Modifier = CommonsSimpleImage75Modifier
val SimpleImage35Modifier = CommonsSimpleImage35Modifier

val SimpleImageBorder = CommonsSimpleImageBorder

val SimpleHeaderImage = CommonsSimpleHeaderImage

val BadgePictureModifier = CommonsBadgePictureModifier

val MaxWidthPaddingTop5dp = CommonsMaxWidthPaddingTop5dp

val VoiceHeightModifier = CommonsVoiceHeightModifier

val PaddingHorizontal12Modifier = CommonsPaddingHorizontal12Modifier

val QuickActionPopupShadow = CommonsQuickActionPopupShadow

val SpacedBy2dp = CommonsSpacedBy2dp
val SpacedBy3dp = CommonsSpacedBy3dp
val SpacedBy5dp = CommonsSpacedBy5dp
val SpacedBy10dp = CommonsSpacedBy10dp
val SpacedBy55dp = CommonsSpacedBy55dp

val PopupUpEffect = CommonsPopupUpEffect

val Size50ModifierOffset10 = CommonsSize50ModifierOffset10

val SuggestionListDefaultHeightChat = CommonsSuggestionListDefaultHeightChat
val SuggestionListDefaultHeightPage = CommonsSuggestionListDefaultHeightPage

val FollowPackHeaderModifier = CommonsFollowPackHeaderModifier

val Size22ModifierWith4Padding = CommonsSize22ModifierWith4Padding

val TextStyleBottomNavBar = CommonsTextStyleBottomNavBar
