package com.doxart.aurakit.components.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.doxart.aurakit.theme.AuraTheme

/**
 * Color configuration for [AuraBarChart].
 */
@Immutable
data class AuraBarChartColors(
    val selectedBarColor: Color,
    val unselectedBarColor: Color,
    val stripeColor: Color,
    val tooltipContainerColor: Color,
    val tooltipContentColor: Color,
    val labelSelectedColor: Color,
    val labelUnselectedColor: Color
)

/**
 * Color configuration for [AuraLineChart].
 */
@Immutable
data class AuraLineChartColors(
    val lineColor: Color,
    val gradientStartColor: Color,
    val gradientEndColor: Color,
    val gridLineColor: Color,
    val pointIndicatorColor: Color,
    val tooltipContainerColor: Color,
    val tooltipContentColor: Color
)

/**
 * Default styling and metrics for AuraKit chart components.
 */
object AuraChartDefaults {

    /** Default maximum height for bar chart columns */
    val MaxBarHeight: Dp = 120.dp

    /** Default line stroke width for line charts */
    val StrokeWidth: Dp
        @Composable
        get() = AuraTheme.sizing.strokeMedium

    /**
     * Resolves default [AuraBarChartColors] referencing current [AuraTheme.colors].
     */
    @Composable
    fun barColors(
        selectedBarColor: Color = AuraTheme.colors.textPrimary,
        unselectedBarColor: Color = AuraTheme.colors.surfaceVariant.copy(alpha = 0.35f),
        stripeColor: Color = AuraTheme.colors.outlineVariant.copy(alpha = 0.40f),
        tooltipContainerColor: Color = AuraTheme.colors.textPrimary,
        tooltipContentColor: Color = AuraTheme.colors.background,
        labelSelectedColor: Color = AuraTheme.colors.textPrimary,
        labelUnselectedColor: Color = AuraTheme.colors.textSecondary
    ): AuraBarChartColors = AuraBarChartColors(
        selectedBarColor = selectedBarColor,
        unselectedBarColor = unselectedBarColor,
        stripeColor = stripeColor,
        tooltipContainerColor = tooltipContainerColor,
        tooltipContentColor = tooltipContentColor,
        labelSelectedColor = labelSelectedColor,
        labelUnselectedColor = labelUnselectedColor
    )

    /**
     * Resolves default [AuraLineChartColors] referencing current [AuraTheme.colors].
     */
    @Composable
    fun lineColors(
        lineColor: Color = AuraTheme.colors.secondary,
        gradientStartColor: Color = AuraTheme.colors.secondary.copy(alpha = 0.35f),
        gradientEndColor: Color = Color.Transparent,
        gridLineColor: Color = AuraTheme.colors.outlineVariant.copy(alpha = 0.15f),
        pointIndicatorColor: Color = AuraTheme.colors.secondary,
        tooltipContainerColor: Color = AuraTheme.colors.surfaceElevated,
        tooltipContentColor: Color = AuraTheme.colors.textPrimary
    ): AuraLineChartColors = AuraLineChartColors(
        lineColor = lineColor,
        gradientStartColor = gradientStartColor,
        gradientEndColor = gradientEndColor,
        gridLineColor = gridLineColor,
        pointIndicatorColor = pointIndicatorColor,
        tooltipContainerColor = tooltipContainerColor,
        tooltipContentColor = tooltipContentColor
    )
}
