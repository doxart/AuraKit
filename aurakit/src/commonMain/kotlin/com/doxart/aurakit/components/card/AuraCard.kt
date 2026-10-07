package com.doxart.aurakit.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.doxart.aurakit.theme.AuraTheme

enum class AuraCardVariant {
    Filled,
    Elevated,
    Outlined
}

@Immutable
data class AuraCardColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color = Color.Transparent
)

object AuraCardDefaults {
    @Composable
    fun cardColors(
        variant: AuraCardVariant = AuraCardVariant.Filled,
        containerColor: Color = when (variant) {
            AuraCardVariant.Filled -> AuraTheme.colors.surfaceVariant
            AuraCardVariant.Elevated -> AuraTheme.colors.surfaceElevated
            AuraCardVariant.Outlined -> AuraTheme.colors.surface
        },
        contentColor: Color = AuraTheme.colors.textPrimary,
        borderColor: Color = when (variant) {
            AuraCardVariant.Outlined -> AuraTheme.colors.outline
            AuraCardVariant.Elevated -> AuraTheme.colors.outlineVariant
            AuraCardVariant.Filled -> Color.Transparent
        }
    ): AuraCardColors = AuraCardColors(
        containerColor = containerColor,
        contentColor = contentColor,
        borderColor = borderColor
    )
}

@Composable
fun AuraCard(
    modifier: Modifier = Modifier,
    variant: AuraCardVariant = AuraCardVariant.Filled,
    shape: Shape = AuraTheme.shapes.lg,
    colors: AuraCardColors = AuraCardDefaults.cardColors(variant = variant),
    borderWidth: Dp = AuraTheme.sizing.strokeThin,
    onClick: (() -> Unit)? = null,
    contentPadding: Dp = AuraTheme.spacing.base,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val baseModifier = modifier
        .clip(shape)
        .background(colors.containerColor, shape)
        .then(
            if (colors.borderColor != Color.Transparent) {
                Modifier.border(BorderStroke(borderWidth, colors.borderColor), shape)
            } else {
                Modifier
            }
        )
        .then(
            if (onClick != null) {
                Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = ripple(color = colors.contentColor.copy(alpha = 0.12f)),
                    onClick = onClick
                )
            } else {
                Modifier
            }
        )
        .padding(contentPadding)

    Box(
        modifier = baseModifier,
        content = content
    )
}
