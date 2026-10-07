package com.doxart.aurakit.components.chart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.doxart.aurakit.theme.AuraTheme
import kotlin.math.roundToInt

/**
 * Pure Canvas-based line chart with smooth cubic bezier curves, gradient fills,
 * subtle horizontal grid lines, and interactive touch scrubbing.
 *
 * @param data Sequence of coordinates along the horizontal axis.
 * @param modifier Layout modifier applied to the chart canvas box.
 * @param colors Color styling resolved via [AuraChartDefaults.lineColors].
 * @param strokeWidth Stroke thickness for the bezier curve.
 * @param gridLineCount Number of horizontal guide lines in the background. Set to 0 to disable.
 * @param animateEntry Whether to smoothly animate the curve vertically on initial composition.
 * @param interactiveScrubbing Whether user can drag/tap along the chart to inspect points.
 * @param onPointSelected Callback triggered when a data point is selected/scrubbed or dismissed (null).
 */
@Composable
fun AuraLineChart(
    data: List<AuraPointData>,
    modifier: Modifier = Modifier,
    colors: AuraLineChartColors = AuraChartDefaults.lineColors(),
    strokeWidth: Dp = AuraChartDefaults.StrokeWidth,
    gridLineCount: Int = 5,
    animateEntry: Boolean = true,
    interactiveScrubbing: Boolean = true,
    onPointSelected: ((AuraPointData?) -> Unit)? = null
) {
    if (data.size < 2) {
        Box(modifier = modifier)
        return
    }

    val animProgress = remember { Animatable(if (animateEntry) 0f else 1f) }
    LaunchedEffect(data) {
        if (animateEntry) {
            animProgress.snapTo(0f)
            animProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
            )
        }
    }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var scrubPixelX by remember { mutableStateOf<Float?>(null) }

    val density = LocalDensity.current
    val strokeWidthPx = with(density) { strokeWidth.toPx() }

    BoxWithConstraints(
        modifier = modifier
    ) {
        val totalWidth = constraints.maxWidth.toFloat()
        val totalHeight = constraints.maxHeight.toFloat()

        // 1. Background Grid Lines
        if (gridLineCount > 0) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(gridLineCount) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.gridLineColor
                    )
                }
            }
        }

        // 2. Pure Canvas Line & Area Drawing
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (interactiveScrubbing) {
                        Modifier.pointerInput(data) {
                            detectTapGestures(
                                onPress = { offset ->
                                    val count = data.size
                                    val step = size.width.toFloat() / (count - 1).toFloat()
                                    val index = ((offset.x + step / 2f) / step)
                                        .toInt()
                                        .coerceIn(0, count - 1)
                                    selectedIndex = index
                                    scrubPixelX = index * step
                                    onPointSelected?.invoke(data[index])
                                }
                            )
                        }.pointerInput(data) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val count = data.size
                                    val step = size.width.toFloat() / (count - 1).toFloat()
                                    val index = ((offset.x + step / 2f) / step)
                                        .toInt()
                                        .coerceIn(0, count - 1)
                                    selectedIndex = index
                                    scrubPixelX = index * step
                                    onPointSelected?.invoke(data[index])
                                },
                                onDragEnd = {
                                    selectedIndex = null
                                    scrubPixelX = null
                                    onPointSelected?.invoke(null)
                                },
                                onDragCancel = {
                                    selectedIndex = null
                                    scrubPixelX = null
                                    onPointSelected?.invoke(null)
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    val count = data.size
                                    val step = size.width.toFloat() / (count - 1).toFloat()
                                    val index = ((change.position.x + step / 2f) / step)
                                        .toInt()
                                        .coerceIn(0, count - 1)
                                    selectedIndex = index
                                    scrubPixelX = index * step
                                    onPointSelected?.invoke(data[index])
                                }
                            )
                        }
                    } else Modifier
                )
        ) {
            val count = data.size
            if (count < 2) return@Canvas

            val minY = data.minOf { it.yValue }
            val maxY = data.maxOf { it.yValue }.let { if (it == minY) it + 1f else it }
            val range = maxY - minY

            val verticalPadding = size.height * 0.12f
            val usableHeight = size.height - (verticalPadding * 2f)
            val currentProgress = animProgress.value

            val stepX = size.width / (count - 1).toFloat()

            val points = data.mapIndexed { index, point ->
                val norm = ((point.yValue - minY) / range) * currentProgress
                val x = index * stepX
                val y = size.height - verticalPadding - (norm * usableHeight)
                Offset(x, y)
            }

            // Path calculation using smooth cubic bezier
            val strokePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 0 until points.size - 1) {
                    val p0 = points[i]
                    val p1 = points[i + 1]
                    val cx = (p0.x + p1.x) / 2f
                    cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                }
            }

            // Area fill path
            val fillPath = Path().apply {
                addPath(strokePath)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            val highestY = points.minOf { it.y }

            // Draw Area Fill
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.gradientStartColor,
                        colors.gradientStartColor.copy(alpha = 0.10f),
                        colors.gradientEndColor
                    ),
                    startY = highestY,
                    endY = size.height
                )
            )

            // Draw Stroke Curve
            drawPath(
                path = strokePath,
                color = colors.lineColor,
                style = Stroke(
                    width = strokeWidthPx,
                    cap = StrokeCap.Round
                )
            )

            // Draw active scrubbing indicator
            val currentScrubIndex = selectedIndex
            if (currentScrubIndex != null && currentScrubIndex in points.indices) {
                val pt = points[currentScrubIndex]

                // Vertical guideline
                drawLine(
                    color = colors.pointIndicatorColor.copy(alpha = 0.5f),
                    start = Offset(pt.x, 0f),
                    end = Offset(pt.x, size.height),
                    strokeWidth = 1.dp.toPx()
                )

                // Point circle
                drawCircle(
                    color = colors.pointIndicatorColor,
                    radius = 5.dp.toPx(),
                    center = pt
                )
                drawCircle(
                    color = Color.White,
                    radius = 2.5.dp.toPx(),
                    center = pt
                )
            }
        }

        // 3. Scrubbing Tooltip Popup
        selectedIndex?.let { index ->
            if (index in data.indices) {
                val point = data[index]
                val displayText = point.formattedValue ?: point.yValue.toString()

                val stepX = totalWidth / (data.size - 1)
                val targetX = (index * stepX).coerceIn(40f, totalWidth - 40f)

                Surface(
                    modifier = Modifier
                        .offset { IntOffset(targetX.roundToInt() - 40, 4) }
                        .clip(AuraTheme.shapes.xs),
                    color = colors.tooltipContainerColor,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = AuraTheme.spacing.xs, vertical = AuraTheme.spacing.xxs),
                        text = displayText,
                        style = AuraTheme.typography.caption.copy(color = colors.tooltipContentColor)
                    )
                }
            }
        }
    }
}
