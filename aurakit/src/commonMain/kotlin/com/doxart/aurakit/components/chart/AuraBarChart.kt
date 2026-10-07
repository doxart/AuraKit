package com.doxart.aurakit.components.chart

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.doxart.aurakit.theme.AuraTheme

/**
 * Isolated multiplatform Bar Chart displaying proportional columns with
 * striped diagonal hatch patterns on inactive bars, animated selection tooltips,
 * and customizable colors.
 *
 * @param data List of [AuraBarData] representing column items.
 * @param modifier Layout modifier applied to the outer container.
 * @param selectedIndex The index of the currently active/selected bar. If null, internal state is used.
 * @param onBarClick Callback invoked when a bar is tapped.
 * @param colors Styling palette resolved via [AuraChartDefaults.barColors].
 * @param maxBarHeight Maximum vertical extent of the highest bar.
 */
@Composable
fun AuraBarChart(
    data: List<AuraBarData>,
    modifier: Modifier = Modifier,
    selectedIndex: Int? = null,
    onBarClick: ((Int, AuraBarData) -> Unit)? = null,
    colors: AuraBarChartColors = AuraChartDefaults.barColors(),
    maxBarHeight: Dp = AuraChartDefaults.MaxBarHeight
) {
    if (data.isEmpty()) {
        Box(modifier = modifier)
        return
    }

    var internalSelectedIndex by remember(data) {
        mutableIntStateOf(selectedIndex ?: (data.size - 1))
    }
    val activeIndex = selectedIndex ?: internalSelectedIndex

    val maxValue = remember(data) {
        data.maxOfOrNull { it.value }?.takeIf { it > 0f } ?: 1f
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.md)
    ) {
        data.forEachIndexed { index, item ->
            val isSelected = index == activeIndex
            val targetRatio = (item.value / maxValue).coerceIn(0.08f, 1f)
            val animatedHeight by animateDpAsState(
                targetValue = maxBarHeight * targetRatio,
                animationSpec = tween(durationMillis = 350),
                label = "AuraBarChartHeightAnimation"
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xs, alignment = Alignment.Bottom)
            ) {
                // 1. Tooltip / Data Bubble
                Box(
                    modifier = Modifier.height(24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    if (isSelected) {
                        Surface(
                            shape = AuraTheme.shapes.xs,
                            color = colors.tooltipContainerColor
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = AuraTheme.spacing.xs,
                                    vertical = AuraTheme.spacing.xxs
                                ),
                                text = item.formattedValue ?: item.value.toInt().toString(),
                                maxLines = 1,
                                style = AuraTheme.typography.caption.copy(
                                    color = colors.tooltipContentColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                // 2. Bar Column with Striped Hatch Pattern
                val barShape = AuraTheme.shapes.sm
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(animatedHeight)
                        .clip(barShape)
                        .background(if (isSelected) (item.color ?: colors.selectedBarColor) else colors.unselectedBarColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                internalSelectedIndex = index
                                onBarClick?.invoke(index, item)
                            }
                        )
                        .drawBehind {
                            if (!isSelected) {
                                val stripeWidth = 6.dp.toPx()
                                val gap = 14.dp.toPx()
                                val step = stripeWidth + gap
                                val totalLength = size.width + size.height

                                var offset = -size.height
                                while (offset < totalLength) {
                                    drawLine(
                                        color = colors.stripeColor,
                                        start = Offset(x = offset, y = size.height),
                                        end = Offset(x = offset + size.height, y = 0f),
                                        strokeWidth = stripeWidth
                                    )
                                    offset += step
                                }
                            }
                        }
                )

                // 3. Category / Month Label
                Text(
                    text = item.label,
                    style = AuraTheme.typography.caption.copy(
                        color = if (isSelected) colors.labelSelectedColor else colors.labelUnselectedColor,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}
