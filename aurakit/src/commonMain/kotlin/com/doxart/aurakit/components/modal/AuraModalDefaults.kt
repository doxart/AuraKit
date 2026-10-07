package com.doxart.aurakit.components.modal

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.doxart.aurakit.theme.AuraTheme

/**
 * Styling configuration for [AuraModalDialog].
 */
@Immutable
data class AuraModalColors(
    val scrimColor: Color,
    val containerColor: Color,
    val contentColor: Color,
    val dragHandleColor: Color,
    val borderColor: Color = Color.Transparent
)

/**
 * Default metrics and styling factory for AuraKit bottom sheets and modal dialogs.
 */
object AuraModalDefaults {

    /** Default fraction of content height dragged before auto-dismissing (35%) */
    const val DismissDragThresholdFraction = 0.35f

    /** Fling velocity threshold (in px/sec) that triggers dismissal regardless of distance */
    const val DismissVelocityThreshold = 1200f

    /** Max height percentage of viewport occupied by modal content (94%) */
    const val MaxModalHeightFraction = 0.94f

    /** Default sheet corner shape with curved top corners */
    val Shape: CornerBasedShape
        @Composable
        get() = RoundedCornerShape(
            topStart = AuraTheme.shapes.corners.xl,
            topEnd = AuraTheme.shapes.corners.xl,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        )

    /** Slide entry animation spec */
    val SlideEnterSpec: androidx.compose.animation.core.FiniteAnimationSpec<IntOffset> = tween(durationMillis = 320, easing = FastOutSlowInEasing)

    /** Slide exit animation spec */
    val SlideExitSpec: androidx.compose.animation.core.FiniteAnimationSpec<IntOffset> = tween(durationMillis = 260, easing = FastOutSlowInEasing)

    /** Rebound spring for when drag threshold is not exceeded */
    val SnapBackSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    /**
     * Resolves default [AuraModalColors] referencing current [AuraTheme.colors].
     */
    @Composable
    fun colors(
        scrimColor: Color = AuraTheme.colors.background.copy(alpha = 0.70f),
        containerColor: Color = AuraTheme.colors.background,
        contentColor: Color = AuraTheme.colors.textPrimary,
        dragHandleColor: Color = AuraTheme.colors.outlineVariant.copy(alpha = 0.60f),
        borderColor: Color = AuraTheme.colors.outlineVariant.copy(alpha = 0.25f)
    ): AuraModalColors = AuraModalColors(
        scrimColor = scrimColor,
        containerColor = containerColor,
        contentColor = contentColor,
        dragHandleColor = dragHandleColor,
        borderColor = borderColor
    )
}
