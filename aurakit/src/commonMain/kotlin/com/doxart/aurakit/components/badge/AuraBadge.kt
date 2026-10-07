package com.doxart.aurakit.components.badge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.doxart.aurakit.theme.AuraTheme

enum class AuraBadgeVariant {
    Primary,
    Secondary,
    Accent,
    Success,
    Warning,
    Error,
    Info,
    Neutral
}

enum class AuraBadgeStyle {
    Filled,
    Subtle,
    Outlined
}

enum class AuraBadgeSize {
    Small,
    Medium
}

@Immutable
data class AuraBadgeColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color = Color.Transparent
)

object AuraBadgeDefaults {
    @Composable
    fun badgeColors(
        variant: AuraBadgeVariant = AuraBadgeVariant.Primary,
        style: AuraBadgeStyle = AuraBadgeStyle.Subtle
    ): AuraBadgeColors {
        val baseColor = when (variant) {
            AuraBadgeVariant.Primary -> AuraTheme.colors.primary
            AuraBadgeVariant.Secondary -> AuraTheme.colors.secondary
            AuraBadgeVariant.Accent -> AuraTheme.colors.accent
            AuraBadgeVariant.Success -> AuraTheme.colors.success
            AuraBadgeVariant.Warning -> AuraTheme.colors.warning
            AuraBadgeVariant.Error -> AuraTheme.colors.error
            AuraBadgeVariant.Info -> AuraTheme.colors.info
            AuraBadgeVariant.Neutral -> AuraTheme.colors.textSecondary
        }

        val onBaseColor = when (variant) {
            AuraBadgeVariant.Primary -> AuraTheme.colors.onPrimary
            AuraBadgeVariant.Secondary -> AuraTheme.colors.onSecondary
            AuraBadgeVariant.Accent -> AuraTheme.colors.onAccent
            AuraBadgeVariant.Success -> AuraTheme.colors.onSuccess
            AuraBadgeVariant.Warning -> AuraTheme.colors.onWarning
            AuraBadgeVariant.Error -> AuraTheme.colors.onError
            AuraBadgeVariant.Info -> AuraTheme.colors.onInfo
            AuraBadgeVariant.Neutral -> AuraTheme.colors.background
        }

        return when (style) {
            AuraBadgeStyle.Filled -> AuraBadgeColors(
                containerColor = baseColor,
                contentColor = onBaseColor,
                borderColor = Color.Transparent
            )
            AuraBadgeStyle.Subtle -> AuraBadgeColors(
                containerColor = baseColor.copy(alpha = 0.15f),
                contentColor = baseColor,
                borderColor = Color.Transparent
            )
            AuraBadgeStyle.Outlined -> AuraBadgeColors(
                containerColor = Color.Transparent,
                contentColor = baseColor,
                borderColor = baseColor.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun AuraBadge(
    modifier: Modifier = Modifier,
    variant: AuraBadgeVariant = AuraBadgeVariant.Primary,
    style: AuraBadgeStyle = AuraBadgeStyle.Subtle,
    size: AuraBadgeSize = AuraBadgeSize.Medium,
    shape: Shape = AuraTheme.shapes.pill,
    colors: AuraBadgeColors = AuraBadgeDefaults.badgeColors(variant = variant, style = style),
    leadingIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val padding = when (size) {
        AuraBadgeSize.Small -> PaddingValues(
            horizontal = AuraTheme.spacing.sm,
            vertical = AuraTheme.spacing.xxs
        )
        AuraBadgeSize.Medium -> PaddingValues(
            horizontal = AuraTheme.spacing.md,
            vertical = AuraTheme.spacing.xs
        )
    }

    val textStyle = when (size) {
        AuraBadgeSize.Small -> AuraTheme.typography.caption
        AuraBadgeSize.Medium -> AuraTheme.typography.labelSmall
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.containerColor, shape)
            .then(
                if (colors.borderColor != Color.Transparent) {
                    Modifier.border(
                        BorderStroke(AuraTheme.sizing.strokeThin, colors.borderColor),
                        shape
                    )
                } else {
                    Modifier
                }
            )
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xs)
        ) {
            if (leadingIcon != null) {
                leadingIcon()
            }
            ProvideTextStyle(value = textStyle.copy(color = colors.contentColor)) {
                content()
            }
        }
    }
}
