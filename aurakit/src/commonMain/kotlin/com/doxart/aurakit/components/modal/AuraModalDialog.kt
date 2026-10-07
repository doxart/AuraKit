package com.doxart.aurakit.components.modal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.doxart.aurakit.getFullScreenDialogProperties
import com.doxart.aurakit.theme.AuraTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Universal modal bottom-sheet container for Compose Multiplatform.
 *
 * Implements physics-based drag-to-dismiss gestures with velocity checking,
 * spring snapback, safe drawing insets, and customizable scrim backdrop.
 *
 * @param onDismissRequest Triggered when user swipes down, clicks scrim, or presses back.
 * @param modifier Modifier applied to the sheet content container.
 * @param alignment Positioning within the viewport (defaults to [Alignment.BottomCenter]).
 * @param shape Corner clipping for the dialog sheet container.
 * @param colors Styling palette resolved via [AuraModalDefaults.colors].
 * @param showDragHandle Whether to render a centered drag indicator capsule at the top.
 * @param swipeToDismissEnabled Enables or disables vertical drag dismissal gestures.
 * @param content Generic slot rendering any composable, providing a [dismiss] trigger.
 */
@Composable
fun AuraModalDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.BottomCenter,
    shape: Shape = AuraModalDefaults.Shape,
    colors: AuraModalColors = AuraModalDefaults.colors(),
    showDragHandle: Boolean = true,
    swipeToDismissEnabled: Boolean = true,
    content: @Composable BoxScope.(dismiss: () -> Unit) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }
    var contentHeight by remember { mutableIntStateOf(0) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withFrameNanos { }
        isVisible = true
    }

    val requestClose: () -> Unit = {
        coroutineScope.launch {
            offsetY.snapTo(0f)
            isVisible = false
        }
    }

    Dialog(
        onDismissRequest = requestClose,
        properties = getFullScreenDialogProperties()
    ) {
        val insets = WindowInsets.safeDrawing.asPaddingValues()
        val topPadding = insets.calculateTopPadding()
        val bottomPadding = insets.calculateBottomPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .layout { measurable, constraints ->
                    val topPx = topPadding.roundToPx()
                    val bottomPx = bottomPadding.roundToPx()

                    val placeable = measurable.measure(
                        constraints.copy(
                            maxHeight = constraints.maxHeight + topPx + bottomPx
                        )
                    )

                    layout(placeable.width, placeable.height - topPx - bottomPx) {
                        placeable.placeRelative(0, -topPx)
                    }
                }
                .background(colors.scrimColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    requestClose()
                },
            contentAlignment = alignment
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(AuraModalDefaults.MaxModalHeightFraction)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = AuraModalDefaults.SlideEnterSpec
                    ) + fadeIn(tween(250)),
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = AuraModalDefaults.SlideExitSpec
                    ) + fadeOut(tween(200))
                ) {
                    DisposableEffect(Unit) {
                        onDispose {
                            if (!isVisible) {
                                onDismissRequest()
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.safeDrawing)
                            .onSizeChanged { contentHeight = it.height }
                            .offset { IntOffset(0, offsetY.value.roundToInt()) }
                            .then(
                                if (swipeToDismissEnabled) {
                                    Modifier.draggable(
                                        state = rememberDraggableState { delta ->
                                            coroutineScope.launch {
                                                val newOffset = (offsetY.value + delta).coerceAtLeast(0f)
                                                offsetY.snapTo(newOffset)
                                            }
                                        },
                                        orientation = Orientation.Vertical,
                                        onDragStopped = { velocity ->
                                            coroutineScope.launch {
                                                val threshold = contentHeight * AuraModalDefaults.DismissDragThresholdFraction
                                                if (offsetY.value > threshold || velocity > AuraModalDefaults.DismissVelocityThreshold) {
                                                    offsetY.animateTo(
                                                        targetValue = contentHeight.toFloat(),
                                                        animationSpec = tween(180, easing = FastOutSlowInEasing)
                                                    )
                                                    onDismissRequest()
                                                } else {
                                                    offsetY.animateTo(
                                                        targetValue = 0f,
                                                        animationSpec = AuraModalDefaults.SnapBackSpring
                                                    )
                                                }
                                            }
                                        }
                                    )
                                } else Modifier
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { /* Prevent dismissing when clicking the modal sheet itself */ }
                            .clip(shape)
                            .background(colors.containerColor)
                            .then(
                                if (colors.borderColor != Color.Transparent) {
                                    Modifier.border(1.dp, colors.borderColor, shape)
                                } else Modifier
                            )
                            .then(modifier)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (showDragHandle) {
                                Spacer(modifier = Modifier.height(AuraTheme.spacing.sm))
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(4.dp)
                                        .clip(AuraTheme.shapes.pill)
                                        .background(colors.dragHandleColor)
                                )
                                Spacer(modifier = Modifier.height(AuraTheme.spacing.xs))
                            }

                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                content(requestClose)
                            }
                        }
                    }
                }
            }
        }
    }
}
