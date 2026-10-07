package com.doxart.aurakit.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.doxart.aurakit.theme.AuraTheme

@Immutable
data class AuraListItemColors(
    val headlineColor: Color,
    val supportingTextColor: Color,
    val trailingContentColor: Color,
    val leadingContainerColor: Color,
    val leadingContentColor: Color
)

object AuraListItemDefaults {
    @Composable
    fun colors(
        headlineColor: Color = AuraTheme.colors.textPrimary,
        supportingTextColor: Color = AuraTheme.colors.textSecondary,
        trailingContentColor: Color = AuraTheme.colors.textPrimary,
        leadingContainerColor: Color = AuraTheme.colors.surfaceVariant,
        leadingContentColor: Color = AuraTheme.colors.textPrimary
    ): AuraListItemColors = AuraListItemColors(
        headlineColor = headlineColor,
        supportingTextColor = supportingTextColor,
        trailingContentColor = trailingContentColor,
        leadingContainerColor = leadingContainerColor,
        leadingContentColor = leadingContentColor
    )
}

/**
 * Standardized, versatile list row component for AuraKit.
 * Can be used standalone, inside cards, or wrapped in [com.doxart.aurakit.components.reveal.AuraRevealableItem].
 */
@Composable
fun AuraListItem(
    headline: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    trailingText: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    leadingShape: Shape = AuraTheme.shapes.md,
    leadingSize: Dp = AuraTheme.sizing.avatarMd,
    horizontalPadding: Dp = AuraTheme.spacing.base,
    verticalPadding: Dp = AuraTheme.spacing.md,
    colors: AuraListItemColors = AuraListItemDefaults.colors(),
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = colors.headlineColor.copy(alpha = 0.1f)),
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.base),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading Area
        if (leadingContent != null) {
            leadingContent()
        } else if (leadingIcon != null) {
            Box(
                modifier = Modifier
                    .size(leadingSize)
                    .clip(leadingShape)
                    .background(colors.leadingContainerColor, leadingShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = colors.leadingContentColor,
                    modifier = Modifier.size(AuraTheme.sizing.iconLg)
                )
            }
        }

        // Center Headline & Supporting Text
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xxs)
        ) {
            Text(
                text = headline,
                style = AuraTheme.typography.titleMedium,
                color = colors.headlineColor,
                maxLines = 1
            )
            if (supportingText != null) {
                Text(
                    text = supportingText,
                    style = AuraTheme.typography.bodySmall,
                    color = colors.supportingTextColor,
                    maxLines = 1
                )
            }
        }

        // Trailing Area
        if (trailingContent != null) {
            trailingContent()
        } else if (trailingText != null) {
            Text(
                text = trailingText,
                style = AuraTheme.typography.titleLarge,
                color = colors.trailingContentColor,
                textAlign = TextAlign.End
            )
        }
    }
}
