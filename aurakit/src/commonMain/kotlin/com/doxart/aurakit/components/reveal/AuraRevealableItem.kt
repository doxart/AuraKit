package com.doxart.aurakit.components.reveal

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import com.doxart.aurakit.theme.AuraTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@Stable
class AuraRevealableItemState(
    internal val animatable: Animatable<Float, AnimationVector1D> = Animatable(0f)
) {
    val currentValue: Float get() = animatable.value
    val isRevealed: Boolean get() = animatable.value < -4f

    suspend fun reset(durationMs: Int = 200) {
        animatable.animateTo(0f, tween(durationMs))
    }

    suspend fun snapToZero() {
        animatable.snapTo(0f)
    }
}

@Composable
fun rememberAuraRevealableItemState(): AuraRevealableItemState {
    return remember { AuraRevealableItemState() }
}

@Immutable
data class AuraRevealAction(
    val id: String,
    val icon: ImageVector,
    val contentDescription: String,
    val containerColor: Color,
    val contentColor: Color,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit
)

@Immutable
data class AuraRevealColors(
    val surfaceColor: Color,
    val revealedSurfaceColor: Color,
    val dividerColor: Color
)

object AuraRevealDefaults {
    @Composable
    fun colors(
        surfaceColor: Color = AuraTheme.colors.surface,
        revealedSurfaceColor: Color = AuraTheme.colors.surfaceVariant,
        dividerColor: Color = AuraTheme.colors.outlineVariant
    ): AuraRevealColors = AuraRevealColors(
        surfaceColor = surfaceColor,
        revealedSurfaceColor = revealedSurfaceColor,
        dividerColor = dividerColor
    )
}

/**
 * A generic swipe-to-reveal container component for Jetpack Compose Multiplatform.
 *
 * Supports:
 * - Physics-based spring action reveals
 * - Elastic overscroll stretch for the terminal/destructive action
 * - Smooth corner radius morphing on reveal
 * - Surface color transition
 * - Threshold-based full swipe trigger
 * - Non-destructive action fade-out during deep drag
 */
@Composable
fun AuraRevealableItem(
    actions: List<AuraRevealAction>,
    modifier: Modifier = Modifier,
    state: AuraRevealableItemState = rememberAuraRevealableItemState(),
    unrevealedCorner: Dp = AuraTheme.shapes.corners.none,
    revealedCorner: Dp = AuraTheme.shapes.corners.full,
    actionButtonSize: Dp = AuraTheme.sizing.actionButtonSize,
    actionSpacing: Dp = AuraTheme.spacing.sm,
    endPadding: Dp = AuraTheme.spacing.base,
    actionShape: Shape = AuraTheme.shapes.md,
    fullSwipeThreshold: Dp = AuraTheme.sizing.swipeThreshold,
    onFullSwipe: (() -> Unit)? = null,
    showDivider: Boolean = true,
    dividerPaddingStart: Dp = AuraTheme.spacing.none,
    colors: AuraRevealColors = AuraRevealDefaults.colors(),
    content: @Composable (isRevealed: Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val motion = AuraTheme.motion
    val offsetX = state.animatable

    val buttonSizePx = with(density) { actionButtonSize.toPx() }
    val spacingPx = with(density) { actionSpacing.toPx() }
    val endPaddingPx = with(density) { endPadding.toPx() }

    val actionCount = actions.size
    val actionsWidthPx = if (actionCount > 0) {
        (buttonSizePx * actionCount) + (spacingPx * (actionCount - 1).coerceAtLeast(0)) + (endPaddingPx * 2)
    } else {
        0f
    }

    var isDragging by remember { mutableStateOf(false) }

    val isRevealed by remember {
        derivedStateOf {
            isDragging || offsetX.value < -4f
        }
    }

    val extraDragPx by remember(actionsWidthPx) {
        derivedStateOf {
            (-offsetX.value - actionsWidthPx).coerceAtLeast(0f)
        }
    }

    val fullSwipeThresholdPx = with(density) { fullSwipeThreshold.toPx() }
    val isFullSwipeActive by remember {
        derivedStateOf { extraDragPx > fullSwipeThresholdPx }
    }

    val animatedContainerColor by animateColorAsState(
        targetValue = if (isRevealed) colors.revealedSurfaceColor else colors.surfaceColor,
        animationSpec = tween(motion.durationShortMs),
        label = "AuraRevealContainerColor"
    )

    val animatedCornerRadius by animateDpAsState(
        targetValue = if (isRevealed) revealedCorner else unrevealedCorner,
        animationSpec = motion.dpSpringSnappy,
        label = "AuraRevealCornerMorph"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .then(
                    if (actions.isNotEmpty()) {
                        Modifier.pointerInput(actionsWidthPx) {
                            detectHorizontalDragGestures(
                                onDragStart = {
                                    isDragging = true
                                },
                                onDragEnd = {
                                    isDragging = false
                                    scope.launch {
                                        if (isFullSwipeActive && onFullSwipe != null) {
                                            offsetX.animateTo(-1500f, tween(250))
                                            onFullSwipe()
                                        } else {
                                            val target = if (offsetX.value < -actionsWidthPx / 2f) {
                                                -actionsWidthPx
                                            } else {
                                                0f
                                            }
                                            offsetX.animateTo(target, tween(200))
                                        }
                                    }
                                },
                                onDragCancel = {
                                    isDragging = false
                                    scope.launch {
                                        offsetX.animateTo(0f, tween(200))
                                    }
                                },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    val targetOffset = (offsetX.value + dragAmount).coerceAtMost(0f)
                                    scope.launch {
                                        offsetX.snapTo(targetOffset)
                                    }
                                }
                            )
                        }
                    } else {
                        Modifier
                    }
                ),
        ) {
            // Background Reveal Actions
            if (actions.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(end = endPadding),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions.forEachIndexed { index, action ->
                        val isLastAction = index == actions.lastIndex
                        val actionRevealThresholdPx = -(buttonSizePx * (actions.size - index) + spacingPx * (actions.size - index - 1) + endPaddingPx * 0.5f)

                        val isActionVisible by remember(actionsWidthPx) {
                            derivedStateOf {
                                if (isLastAction) offsetX.value < -16f else offsetX.value < actionRevealThresholdPx
                            }
                        }

                        val actionScale by animateFloatAsState(
                            targetValue = if (isActionVisible) 1f else 0f,
                            animationSpec = motion.springGentle,
                            label = "AuraActionScale_${action.id}"
                        )

                        if (!isLastAction) {
                            // Non-terminal actions: slide back and fade during deep drag
                            Box(
                                modifier = Modifier.graphicsLayer {
                                    translationX = -extraDragPx * 0.4f
                                    alpha = (1f - (extraDragPx / fullSwipeThresholdPx)).coerceIn(0f, 1f)
                                }
                            ) {
                                AuraRevealActionButton(
                                    action = action,
                                    size = actionButtonSize,
                                    shape = actionShape,
                                    modifier = Modifier.scale(actionScale),
                                    onClick = {
                                        scope.launch { offsetX.animateTo(0f, tween(motion.durationShortMs)) }
                                        action.onClick()
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.width(actionSpacing))
                        } else {
                            // Terminal action: elastic stretch during deep drag
                            val dynamicWidth = with(density) {
                                (buttonSizePx + extraDragPx).toDp()
                            }

                            Box(
                                modifier = Modifier.width(dynamicWidth),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                AuraRevealActionButton(
                                    action = action,
                                    size = actionButtonSize,
                                    shape = actionShape,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .scale(actionScale),
                                    onClick = {
                                        scope.launch { offsetX.animateTo(0f, tween(motion.durationShortMs)) }
                                        action.onClick()
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Foreground Content
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationX = offsetX.value
                    },
                shape = RoundedCornerShape(animatedCornerRadius),
                color = animatedContainerColor
            ) {
                content(isRevealed)
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dividerPaddingStart),
                color = if (!isRevealed) colors.dividerColor else Color.Transparent,
                thickness = AuraTheme.sizing.strokeThin
            )
        }
    }
}

@Composable
private fun AuraRevealActionButton(
    action: AuraRevealAction,
    size: Dp,
    shape: Shape,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(action.containerColor, shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = action.contentColor.copy(alpha = 0.2f)),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = action.icon,
            contentDescription = action.contentDescription,
            tint = action.contentColor,
            modifier = Modifier.size(AuraTheme.sizing.iconLg)
        )
    }
}
