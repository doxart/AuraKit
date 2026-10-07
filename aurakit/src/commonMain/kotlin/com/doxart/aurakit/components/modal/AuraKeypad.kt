package com.doxart.aurakit.components.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.doxart.aurakit.foundation.AuraIcons
import com.doxart.aurakit.theme.AuraTheme

/**
 * Custom in-dialog numeric keypad matching the tactile aesthetic from easywallet.
 * Provides rapid touch-based number entry for currency, decimal, OTP, and integer inputs.
 *
 * @param onDigitClick Invoked with digit string ("0"-"9", ".", "000", "+").
 * @param onBackspace Invoked when the backspace button is tapped.
 * @param modifier Layout modifier applied to the keypad container.
 * @param bottomExtraKey String for bottom-left auxiliary key (e.g. "000", ".", "+", or empty).
 * @param containerColor Background color for individual key cells.
 * @param contentColor Text/Icon color for keypad items.
 */
@Composable
fun AuraKeypad(
    onDigitClick: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier,
    bottomExtraKey: String = "000",
    containerColor: Color = AuraTheme.colors.surfaceVariant.copy(alpha = 0.50f),
    contentColor: Color = AuraTheme.colors.textPrimary
) {
    val keyShape = AuraTheme.shapes.sm

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
    ) {
        val rows = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf(bottomExtraKey, "0", "BACKSPACE")
        )

        rows.forEach { rowKeys ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
            ) {
                rowKeys.forEach { key ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.55f)
                            .clip(keyShape)
                            .background(if (key.isNotBlank()) containerColor else Color.Transparent)
                            .clickable(
                                enabled = key.isNotBlank(),
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    if (key == "BACKSPACE") {
                                        onBackspace()
                                    } else if (key.isNotBlank()) {
                                        onDigitClick(key)
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (key == "BACKSPACE") {
                            Icon(
                                imageVector = AuraIcons.Backspace,
                                contentDescription = "Backspace",
                                tint = contentColor,
                                modifier = Modifier.size(AuraTheme.sizing.iconMd)
                            )
                        } else if (key.isNotBlank()) {
                            Text(
                                text = key,
                                style = AuraTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = contentColor
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
