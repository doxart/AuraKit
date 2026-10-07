package com.doxart.aurakit.components.appbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.doxart.aurakit.theme.AuraTheme

/**
 * Background style variants for [AuraTopAppBar].
 */
enum class AuraTopAppBarStyle {
    /** Completely transparent container, ideal for overlapping hero content or immersive views. */
    Transparent,

    /** Solid surface color matching the current theme canvas/surface. */
    Solid,

    /**
     * Vertical gradient fading from theme background to transparent.
     * Matches the easywallet `BlackToNone` pattern — ideal for content that scrolls
     * beneath the app bar with a soft fade-out.
     */
    Gradient,

    /** Elevated container with a distinct background color for non-transparent scenarios. */
    Elevated
}

/**
 * Color specifications for [AuraTopAppBar].
 */
@Immutable
data class AuraTopAppBarColors(
    val containerColor: Color,
    val titleContentColor: Color,
    val navigationIconContentColor: Color,
    val actionIconContentColor: Color,
    val gradientBrush: Brush? = null
)

/**
 * Default values and styling factory for [AuraTopAppBar].
 */
object AuraTopAppBarDefaults {

    /** Default horizontal padding for TopAppBar content. */
    val HorizontalPadding: Dp
        @Composable
        get() = AuraTheme.spacing.base

    /** Default container height for action elements (matches controlSm = 36.dp). */
    val ActionControlSize: Dp
        @Composable
        get() = AuraTheme.sizing.controlSm

    /**
     * Default multi-stop vertical gradient scrim for transparent and gradient top app bars.
     * Matches the easywallet `BlackToNone` brush, providing a smooth optical fade from
     * the solid theme canvas color to transparent.
     */
    @Composable
    fun defaultTopScrimBrush(
        startColor: Color = AuraTheme.colors.background,
        endColor: Color = Color.Transparent
    ): Brush = Brush.verticalGradient(
        colors = listOf(
            startColor,
            startColor.copy(alpha = 0.85f),
            startColor.copy(alpha = 0.55f),
            startColor.copy(alpha = 0.25f),
            endColor
        )
    )

    /**
     * Resolves [AuraTopAppBarColors] according to the selected [AuraTopAppBarStyle]
     * dynamically using [AuraTheme.colors].
     *
     * Both [AuraTopAppBarStyle.Transparent] and [AuraTopAppBarStyle.Gradient] styles
     * apply the soft multi-stop [defaultTopScrimBrush] underneath, ensuring content
     * scrolling under the bar softly fades without losing contrast or readability.
     */
    @Composable
    fun colors(
        style: AuraTopAppBarStyle = AuraTopAppBarStyle.Transparent,
        containerColor: Color = when (style) {
            AuraTopAppBarStyle.Transparent -> Color.Transparent
            AuraTopAppBarStyle.Solid -> AuraTheme.colors.background
            AuraTopAppBarStyle.Gradient -> Color.Transparent
            AuraTopAppBarStyle.Elevated -> AuraTheme.colors.surfaceElevated
        },
        titleContentColor: Color = AuraTheme.colors.textPrimary,
        navigationIconContentColor: Color = AuraTheme.colors.textPrimary,
        actionIconContentColor: Color = AuraTheme.colors.textPrimary,
        gradientBrush: Brush? = when (style) {
            AuraTopAppBarStyle.Transparent,
            AuraTopAppBarStyle.Gradient -> defaultTopScrimBrush()
            else -> null
        }
    ): AuraTopAppBarColors = AuraTopAppBarColors(
        containerColor = containerColor,
        titleContentColor = titleContentColor,
        navigationIconContentColor = navigationIconContentColor,
        actionIconContentColor = actionIconContentColor,
        gradientBrush = gradientBrush
    )
}
