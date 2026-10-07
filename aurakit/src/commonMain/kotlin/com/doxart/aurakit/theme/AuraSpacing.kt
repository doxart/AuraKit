package com.doxart.aurakit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AuraSpacing(
    val none: Dp = 0.dp,
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val base: Dp = 16.dp,
    val lg: Dp = 20.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
    val xxxl: Dp = 48.dp
)

@Immutable
data class AuraSizing(
    val strokeThin: Dp = 1.dp,
    val strokeMedium: Dp = 2.dp,
    val strokeThick: Dp = 4.dp,
    val iconXs: Dp = 12.dp,
    val iconSm: Dp = 14.dp,
    val iconMd: Dp = 18.dp,
    val iconLg: Dp = 22.dp,
    val iconXl: Dp = 28.dp,
    val iconXxl: Dp = 36.dp,
    val controlSm: Dp = 36.dp,
    val controlMd: Dp = 44.dp,
    val controlLg: Dp = 52.dp,
    val actionButtonSize: Dp = 48.dp,
    val avatarMd: Dp = 48.dp,
    val barHeightExpanded: Dp = 64.dp,
    val barHeightCollapsed: Dp = 48.dp,
    val swipeThreshold: Dp = 96.dp,
    val dividerIndent: Dp = 76.dp
)

val DefaultAuraSpacing = AuraSpacing()
val DefaultAuraSizing = AuraSizing()
