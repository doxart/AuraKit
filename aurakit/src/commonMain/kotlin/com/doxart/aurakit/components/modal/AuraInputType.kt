package com.doxart.aurakit.components.modal

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Sealed hierarchy defining the supported dynamic input modes for [AuraInputDialog].
 *
 * Automatically provides [KeyboardType], [VisualTransformation], formatting helpers,
 * and client-side validation rules.
 */
sealed interface AuraInputType {

    /** Suggested virtual keyboard mode */
    val keyboardType: KeyboardType

    /** Visual transformation applied to the input text */
    val visualTransformation: VisualTransformation

    /** Formats the raw string value for clean visual rendering */
    fun formatDisplay(rawInput: String): String

    /** Validates whether the current raw input satisfies the rules */
    fun validate(rawInput: String): Boolean

    // 1. Currency
    data class Currency(
        val symbol: String = "$"
    ) : AuraInputType {
        override val keyboardType: KeyboardType = KeyboardType.Number
        override val visualTransformation: VisualTransformation = VisualTransformation.None

        override fun formatDisplay(rawInput: String): String {
            val cents = rawInput.filter { it.isDigit() }.toLongOrNull() ?: 0L
            return AuraInputFormatters.formatCentsToCurrency(cents, symbol)
        }

        override fun validate(rawInput: String): Boolean {
            val cents = rawInput.filter { it.isDigit() }.toLongOrNull() ?: 0L
            return cents > 0L
        }
    }

    // 2. Integer
    data class Integer(
        val min: Long? = null,
        val max: Long? = null
    ) : AuraInputType {
        override val keyboardType: KeyboardType = KeyboardType.Number
        override val visualTransformation: VisualTransformation = VisualTransformation.None

        override fun formatDisplay(rawInput: String): String {
            val num = rawInput.filter { it.isDigit() }.toLongOrNull()
            return num?.let { AuraInputFormatters.formatThousands(it) } ?: "0"
        }

        override fun validate(rawInput: String): Boolean {
            val num = rawInput.filter { it.isDigit() }.toLongOrNull() ?: return false
            if (min != null && num < min) return false
            if (max != null && num > max) return false
            return true
        }
    }

    // 3. Decimal
    data class Decimal(
        val decimalPlaces: Int = 2
    ) : AuraInputType {
        override val keyboardType: KeyboardType = KeyboardType.Decimal
        override val visualTransformation: VisualTransformation = VisualTransformation.None

        override fun formatDisplay(rawInput: String): String {
            return if (rawInput.isBlank()) "0.00" else rawInput
        }

        override fun validate(rawInput: String): Boolean {
            val parsed = rawInput.toDoubleOrNull() ?: return false
            return parsed != 0.0
        }
    }

    // 4. Phone Number
    data class Phone(
        val pattern: String = "+X (XXX) XXX-XXXX",
        val minDigits: Int = 10
    ) : AuraInputType {
        override val keyboardType: KeyboardType = KeyboardType.Phone
        override val visualTransformation: VisualTransformation = PhoneVisualTransformation(pattern)

        override fun formatDisplay(rawInput: String): String {
            val digits = rawInput.filter { it.isDigit() }
            return AuraInputFormatters.formatPhone(digits, pattern)
        }

        override fun validate(rawInput: String): Boolean {
            val digits = rawInput.filter { it.isDigit() }
            return digits.length >= minDigits
        }
    }

    // 5. Code / OTP
    data class Code(
        val length: Int = 6,
        val isNumericOnly: Boolean = true
    ) : AuraInputType {
        override val keyboardType: KeyboardType =
            if (isNumericOnly) KeyboardType.NumberPassword else KeyboardType.Ascii
        override val visualTransformation: VisualTransformation = VisualTransformation.None

        override fun formatDisplay(rawInput: String): String {
            val filtered = if (isNumericOnly) rawInput.filter { it.isDigit() } else rawInput
            return filtered.take(length)
        }

        override fun validate(rawInput: String): Boolean {
            val filtered = if (isNumericOnly) rawInput.filter { it.isDigit() } else rawInput
            return filtered.length == length
        }
    }

    // 6. Password / Masked Text
    data class Password(
        val minLength: Int = 6,
        val isMasked: Boolean = true
    ) : AuraInputType {
        override val keyboardType: KeyboardType = KeyboardType.Password
        override val visualTransformation: VisualTransformation =
            if (isMasked) PasswordVisualTransformation() else VisualTransformation.None

        override fun formatDisplay(rawInput: String): String {
            return if (isMasked) "•".repeat(rawInput.length) else rawInput
        }

        override fun validate(rawInput: String): Boolean {
            return rawInput.length >= minLength
        }
    }
}
