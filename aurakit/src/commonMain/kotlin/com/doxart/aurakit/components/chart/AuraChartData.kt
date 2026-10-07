package com.doxart.aurakit.components.chart

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Data item representing a single bar in [AuraBarChart].
 *
 * @param id Unique identifier for the bar item.
 * @param label Text displayed below the bar (e.g., month abbreviation "Apr", day "Mon").
 * @param value Numerical magnitude of the bar.
 * @param formattedValue Human-readable formatted string displayed on the tooltip (e.g. "$2,220").
 *                       If null, value will be formatted as a rounded number.
 * @param color Optional custom highlight color for this specific bar when selected.
 */
@Immutable
data class AuraBarData(
    val id: String,
    val label: String,
    val value: Float,
    val formattedValue: String? = null,
    val color: Color? = null
)

/**
 * Data point representing a coordinate in [AuraLineChart].
 *
 * @param xLabel Category/time label for the point along the horizontal axis.
 * @param yValue Numerical magnitude along the vertical axis.
 * @param formattedValue Human-readable formatted string (e.g., "$1,450.00").
 */
@Immutable
data class AuraPointData(
    val xLabel: String,
    val yValue: Float,
    val formattedValue: String? = null
)
