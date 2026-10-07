package com.doxart.aurakit.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AuraCorners(
    val none: Dp = 0.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
    val full: Dp = 9999.dp
)

@Immutable
data class AuraShapes(
    val corners: AuraCorners = AuraCorners(),
    val none: CornerBasedShape = RoundedCornerShape(0.dp),
    val xs: CornerBasedShape = RoundedCornerShape(4.dp),
    val sm: CornerBasedShape = RoundedCornerShape(8.dp),
    val md: CornerBasedShape = RoundedCornerShape(12.dp),
    val lg: CornerBasedShape = RoundedCornerShape(16.dp),
    val xl: CornerBasedShape = RoundedCornerShape(24.dp),
    val xxl: CornerBasedShape = RoundedCornerShape(32.dp),
    val pill: CornerBasedShape = CircleShape
)

val DefaultAuraShapes = AuraShapes()
