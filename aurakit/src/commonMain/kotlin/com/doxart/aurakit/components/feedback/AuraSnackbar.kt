package com.doxart.aurakit.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.doxart.aurakit.components.button.AuraButton
import com.doxart.aurakit.components.button.AuraButtonSize
import com.doxart.aurakit.components.button.AuraButtonVariant
import com.doxart.aurakit.theme.AuraTheme
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

enum class AuraSnackbarType {
    Default,
    Success,
    Warning,
    Error,
    Info
}

enum class AuraSnackbarResult {
    Dismissed,
    ActionPerformed
}

@Immutable
data class AuraSnackbarData(
    val message: String,
    val title: String? = null,
    val actionLabel: String? = null,
    val type: AuraSnackbarType = AuraSnackbarType.Default,
    val durationMs: Long = 4000L,
    private val continuation: CancellableContinuation<AuraSnackbarResult>
) {
    fun dismiss() {
        if (continuation.isActive) continuation.resume(AuraSnackbarResult.Dismissed)
    }

    fun performAction() {
        if (continuation.isActive) continuation.resume(AuraSnackbarResult.ActionPerformed)
    }
}

class AuraSnackbarHostState {
    private val mutex = Mutex()
    var currentSnackbarData by mutableStateOf<AuraSnackbarData?>(null)
        private set

    suspend fun showSnackbar(
        message: String,
        title: String? = null,
        actionLabel: String? = null,
        type: AuraSnackbarType = AuraSnackbarType.Default,
        durationMs: Long = 4000L
    ): AuraSnackbarResult = mutex.withLock {
        try {
            suspendCancellableCoroutine { continuation ->
                currentSnackbarData = AuraSnackbarData(
                    message = message,
                    title = title,
                    actionLabel = actionLabel,
                    type = type,
                    durationMs = durationMs,
                    continuation = continuation
                )
            }
        } finally {
            currentSnackbarData = null
        }
    }
}

@Composable
fun rememberAuraSnackbarHostState(): AuraSnackbarHostState = remember { AuraSnackbarHostState() }

@Composable
fun AuraSnackbarHost(
    hostState: AuraSnackbarHostState,
    modifier: Modifier = Modifier,
    snackbar: @Composable (AuraSnackbarData) -> Unit = { AuraSnackbar(it) }
) {
    val currentData = hostState.currentSnackbarData

    LaunchedEffect(currentData) {
        if (currentData != null) {
            delay(currentData.durationMs)
            currentData.dismiss()
        }
    }

    AnimatedVisibility(
        visible = currentData != null,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        if (currentData != null) {
            snackbar(currentData)
        }
    }
}

@Composable
fun AuraSnackbar(
    data: AuraSnackbarData,
    modifier: Modifier = Modifier,
    shape: Shape = AuraTheme.shapes.lg
) {
    val accentColor = when (data.type) {
        AuraSnackbarType.Default -> AuraTheme.colors.primary
        AuraSnackbarType.Success -> AuraTheme.colors.success
        AuraSnackbarType.Warning -> AuraTheme.colors.warning
        AuraSnackbarType.Error -> AuraTheme.colors.error
        AuraSnackbarType.Info -> AuraTheme.colors.info
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(AuraTheme.colors.surfaceElevated, shape)
            .border(
                BorderStroke(AuraTheme.sizing.strokeThin, accentColor.copy(alpha = 0.4f)),
                shape
            )
            .padding(AuraTheme.spacing.base)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.md)
        ) {
            Box(
                modifier = Modifier
                    .clip(AuraTheme.shapes.pill)
                    .background(accentColor.copy(alpha = 0.2f))
                    .padding(horizontal = AuraTheme.spacing.sm, vertical = AuraTheme.spacing.xxs)
            ) {
                Text(
                    text = data.type.name.uppercase(),
                    style = AuraTheme.typography.caption,
                    color = accentColor
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xxs)
            ) {
                if (data.title != null) {
                    Text(
                        text = data.title,
                        style = AuraTheme.typography.titleSmall,
                        color = AuraTheme.colors.textPrimary
                    )
                }
                Text(
                    text = data.message,
                    style = AuraTheme.typography.bodySmall,
                    color = AuraTheme.colors.textSecondary
                )
            }

            if (data.actionLabel != null) {
                AuraButton(
                    onClick = { data.performAction() },
                    variant = AuraButtonVariant.Ghost,
                    size = AuraButtonSize.Small
                ) {
                    Text(text = data.actionLabel, color = accentColor)
                }
            }
        }
    }
}
