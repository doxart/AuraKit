package com.doxart.aurakit.components.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.doxart.aurakit.components.button.AuraButton
import com.doxart.aurakit.components.button.AuraButtonSize
import com.doxart.aurakit.components.button.AuraButtonVariant
import com.doxart.aurakit.foundation.AuraIcons
import com.doxart.aurakit.theme.AuraTheme

/**
 * High-level dynamic input dialog built on top of [AuraModalDialog].
 *
 * Supports dynamic input modes ([AuraInputType.Currency], [AuraInputType.Integer],
 * [AuraInputType.Decimal], [AuraInputType.Phone], [AuraInputType.Code], [AuraInputType.Password]),
 * automatic validation, tactile keypad, and seamless design token synchronization.
 *
 * @param onDismissRequest Triggered when user cancels or dismisses the dialog.
 * @param onConfirm Invoked with the finalized input string when confirmed.
 * @param title Header title displayed at top.
 * @param inputType Dynamic input mode and validation engine.
 * @param initialValue Initial string state for the input.
 * @param subtitle Optional explanatory caption below title.
 * @param confirmButtonText Label on the primary action button.
 * @param useBuiltinKeypad When true, provides the tactile numeric keypad for rapid entry.
 */
@Composable
fun AuraInputDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    title: String,
    inputType: AuraInputType = AuraInputType.Currency(),
    initialValue: String = "",
    subtitle: String? = null,
    confirmButtonText: String = "Done",
    useBuiltinKeypad: Boolean = true
) {
    var rawInput by remember(initialValue) { mutableStateOf(initialValue) }
    val isValid = inputType.validate(rawInput)

    AuraModalDialog(
        onDismissRequest = onDismissRequest,
        showDragHandle = true
    ) { dismiss ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AuraTheme.spacing.base,
                    end = AuraTheme.spacing.base,
                    bottom = AuraTheme.spacing.xl,
                    top = AuraTheme.spacing.sm
                ),
            verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.base)
        ) {
            // 1. Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = AuraTheme.typography.titleLarge.copy(
                            color = AuraTheme.colors.textPrimary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            style = AuraTheme.typography.caption.copy(
                                color = AuraTheme.colors.textSecondary
                            ),
                            maxLines = 1
                        )
                    }
                }

                IconButton(
                    onClick = dismiss,
                    modifier = Modifier.size(AuraTheme.sizing.controlSm)
                ) {
                    Icon(
                        imageVector = AuraIcons.Close,
                        contentDescription = "Close",
                        tint = AuraTheme.colors.textSecondary,
                        modifier = Modifier.size(AuraTheme.sizing.iconSm)
                    )
                }
            }

            // 2. Main Value Display Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .clip(AuraTheme.shapes.md)
                    .background(AuraTheme.colors.surfaceVariant.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                when (inputType) {
                    is AuraInputType.Code -> {
                        // Discrete OTP boxes
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val codeLength = inputType.length
                            for (i in 0 until codeLength) {
                                val charAtI = rawInput.getOrNull(i)?.toString() ?: ""
                                val isBoxActive = i == rawInput.length
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(AuraTheme.shapes.sm)
                                        .background(AuraTheme.colors.surface)
                                        .border(
                                            width = if (isBoxActive) 1.5.dp else 1.dp,
                                            color = if (isBoxActive) AuraTheme.colors.primary else AuraTheme.colors.outlineVariant,
                                            shape = AuraTheme.shapes.sm
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = charAtI,
                                        style = AuraTheme.typography.titleLarge.copy(
                                            color = AuraTheme.colors.textPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        val formattedDisplay = inputType.formatDisplay(rawInput)
                        Text(
                            modifier = Modifier.padding(horizontal = AuraTheme.spacing.md),
                            text = formattedDisplay.ifBlank { "0" },
                            style = AuraTheme.typography.displaySmall.copy(
                                color = AuraTheme.colors.textPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // 3. Input Controls: Builtin Keypad OR System TextField
            val showKeypad = useBuiltinKeypad && inputType !is AuraInputType.Password
            if (showKeypad) {
                val bottomKey = when (inputType) {
                    is AuraInputType.Currency -> "000"
                    is AuraInputType.Decimal -> "."
                    is AuraInputType.Phone -> "+"
                    else -> ""
                }

                AuraKeypad(
                    bottomExtraKey = bottomKey,
                    onDigitClick = { digit ->
                        when (inputType) {
                            is AuraInputType.Code -> {
                                if (rawInput.length < inputType.length) {
                                    rawInput += digit
                                }
                            }
                            is AuraInputType.Currency -> {
                                when (digit) {
                                    "000" -> {
                                        val cents = rawInput.filter { it.isDigit() }.toLongOrNull() ?: 0L
                                        if (cents in 1..<100_000_000L) {
                                            rawInput = (cents * 1000).toString()
                                        }
                                    }
                                    else -> {
                                        val cents = rawInput.filter { it.isDigit() }.toLongOrNull() ?: 0L
                                        if (cents < 100_000_000_00L) {
                                            rawInput = (cents * 10 + (digit.toIntOrNull() ?: 0)).toString()
                                        }
                                    }
                                }
                            }
                            else -> {
                                rawInput += digit
                            }
                        }
                    },
                    onBackspace = {
                        if (rawInput.isNotEmpty()) {
                            rawInput = rawInput.dropLast(1)
                        }
                    }
                )
            } else {
                // System TextField input for Password or when Keypad is disabled
                BasicTextField(
                    value = rawInput,
                    onValueChange = { rawInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(AuraTheme.shapes.sm)
                        .background(AuraTheme.colors.surfaceVariant.copy(alpha = 0.5f))
                        .padding(AuraTheme.spacing.base),
                    textStyle = AuraTheme.typography.bodyLarge.copy(color = AuraTheme.colors.textPrimary),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = inputType.keyboardType,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isValid) {
                                onConfirm(rawInput)
                                dismiss()
                            }
                        }
                    ),
                    visualTransformation = inputType.visualTransformation,
                    cursorBrush = SolidColor(AuraTheme.colors.primary),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(AuraTheme.spacing.xs))

            // 4. Confirm Button
            AuraButton(
                onClick = {
                    onConfirm(rawInput)
                    dismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                variant = AuraButtonVariant.Primary,
                size = AuraButtonSize.Large,
                enabled = isValid
            ) {
                Text(confirmButtonText)
            }
        }
    }
}
