package com.doxart.aurakit.components.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import com.doxart.aurakit.theme.AuraTheme

enum class AuraButtonVariant {
    Primary,
    Secondary,
    Outline,
    Ghost,
    Destructive
}

enum class AuraButtonSize {
    Small,
    Medium,
    Large
}

@Immutable
data class AuraButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val borderColor: Color = Color.Transparent,
    val disabledBorderColor: Color = Color.Transparent
)

object AuraButtonDefaults {
    @Composable
    fun buttonColors(
        variant: AuraButtonVariant = AuraButtonVariant.Primary,
        containerColor: Color = when (variant) {
            AuraButtonVariant.Primary -> AuraTheme.colors.primary
            AuraButtonVariant.Secondary -> AuraTheme.colors.surfaceVariant
            AuraButtonVariant.Outline -> Color.Transparent
            AuraButtonVariant.Ghost -> Color.Transparent
            AuraButtonVariant.Destructive -> AuraTheme.colors.error
        },
        contentColor: Color = when (variant) {
            AuraButtonVariant.Primary -> AuraTheme.colors.onPrimary
            AuraButtonVariant.Secondary -> AuraTheme.colors.textPrimary
            AuraButtonVariant.Outline -> AuraTheme.colors.primary
            AuraButtonVariant.Ghost -> AuraTheme.colors.textPrimary
            AuraButtonVariant.Destructive -> AuraTheme.colors.onError
        },
        disabledContainerColor: Color = when (variant) {
            AuraButtonVariant.Outline, AuraButtonVariant.Ghost -> Color.Transparent
            else -> AuraTheme.colors.surfaceVariant.copy(alpha = 0.5f)
        },
        disabledContentColor: Color = AuraTheme.colors.textMuted,
        borderColor: Color = when (variant) {
            AuraButtonVariant.Outline -> AuraTheme.colors.outline
            else -> Color.Transparent
        },
        disabledBorderColor: Color = when (variant) {
            AuraButtonVariant.Outline -> AuraTheme.colors.outlineVariant.copy(alpha = 0.5f)
            else -> Color.Transparent
        }
    ): AuraButtonColors = AuraButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor
    )
}

@Composable
fun AuraButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AuraButtonVariant = AuraButtonVariant.Primary,
    size: AuraButtonSize = AuraButtonSize.Medium,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = when (size) {
        AuraButtonSize.Small -> AuraTheme.shapes.sm
        AuraButtonSize.Medium -> AuraTheme.shapes.md
        AuraButtonSize.Large -> AuraTheme.shapes.lg
    },
    colors: AuraButtonColors = AuraButtonDefaults.buttonColors(variant = variant),
    borderWidth: Dp = AuraTheme.sizing.strokeThin,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled && !isLoading) 0.97f else 1f,
        animationSpec = AuraTheme.motion.springSnappy,
        label = "AuraButtonPressScale"
    )

    val isClickable = enabled && !isLoading
    val currentContainerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val currentContentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val currentBorderColor = if (enabled) colors.borderColor else colors.disabledBorderColor

    val minHeight = when (size) {
        AuraButtonSize.Small -> AuraTheme.sizing.controlSm
        AuraButtonSize.Medium -> AuraTheme.sizing.controlMd
        AuraButtonSize.Large -> AuraTheme.sizing.controlLg
    }

    val contentPadding = when (size) {
        AuraButtonSize.Small -> PaddingValues(horizontal = AuraTheme.spacing.md, vertical = AuraTheme.spacing.xs)
        AuraButtonSize.Medium -> PaddingValues(horizontal = AuraTheme.spacing.base, vertical = AuraTheme.spacing.sm)
        AuraButtonSize.Large -> PaddingValues(horizontal = AuraTheme.spacing.xl, vertical = AuraTheme.spacing.md)
    }

    val textStyle = when (size) {
        AuraButtonSize.Small -> AuraTheme.typography.labelSmall
        AuraButtonSize.Medium -> AuraTheme.typography.labelMedium
        AuraButtonSize.Large -> AuraTheme.typography.labelLarge
    }

    val progressSize = when (size) {
        AuraButtonSize.Small -> AuraTheme.sizing.iconSm
        AuraButtonSize.Medium -> AuraTheme.sizing.iconMd
        AuraButtonSize.Large -> AuraTheme.sizing.iconLg
    }

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(currentContainerColor, shape)
            .then(
                if (currentBorderColor != Color.Transparent) {
                    Modifier.border(BorderStroke(borderWidth, currentBorderColor), shape)
                } else {
                    Modifier
                }
            )
            .then(
                if (isClickable) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(color = currentContentColor.copy(alpha = 0.16f)),
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .defaultMinSize(minHeight = minHeight)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(progressSize),
                color = currentContentColor,
                strokeWidth = AuraTheme.sizing.strokeMedium,
                strokeCap = StrokeCap.Round
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                }

                ProvideTextStyle(value = textStyle.copy(color = currentContentColor)) {
                    content()
                }

                if (trailingIcon != null) {
                    trailingIcon()
                }
            }
        }
    }
}
