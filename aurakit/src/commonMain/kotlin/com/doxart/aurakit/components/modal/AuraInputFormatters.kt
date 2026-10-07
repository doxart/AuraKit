package com.doxart.aurakit.components.modal

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Pure Kotlin Multiplatform utility for currency, phone, and masked number formatting
 * without JVM-specific dependencies.
 */
object AuraInputFormatters {

    /**
     * Formats integer cents into a localized currency string.
     * e.g., 222050 cents with "$" -> "$2,220.50"
     */
    fun formatCentsToCurrency(cents: Long, symbol: String = "$"): String {
        val isNegative = cents < 0
        val absCents = if (isNegative) -cents else cents
        val whole = absCents / 100
        val fraction = absCents % 100

        val wholeStr = formatThousands(whole)
        val fractionStr = if (fraction < 10) "0$fraction" else fraction.toString()

        val prefix = if (isNegative) "-$symbol" else symbol
        return "$prefix$wholeStr.$fractionStr"
    }

    /**
     * Inserts comma grouping every 3 digits.
     */
    fun formatThousands(number: Long): String {
        val str = number.toString()
        if (str.length <= 3) return str

        val builder = StringBuilder()
        val remainder = str.length % 3
        var index = 0

        if (remainder > 0) {
            builder.append(str.substring(0, remainder))
            index = remainder
            if (index < str.length) builder.append(",")
        }

        while (index < str.length) {
            builder.append(str.substring(index, index + 3))
            index += 3
            if (index < str.length) builder.append(",")
        }

        return builder.toString()
    }

    /**
     * Formats plain numeric digits according to a template mask like "+X (XXX) XXX-XXXX".
     */
    fun formatPhone(digits: String, pattern: String = "+X (XXX) XXX-XXXX", maskChar: Char = 'X'): String {
        val cleanDigits = digits.filter { it.isDigit() }
        val result = StringBuilder()
        var digitIndex = 0

        for (ch in pattern) {
            if (digitIndex >= cleanDigits.length) break
            if (ch == maskChar) {
                result.append(cleanDigits[digitIndex])
                digitIndex++
            } else {
                result.append(ch)
            }
        }
        return result.toString()
    }
}

/**
 * VisualTransformation that masks phone numbers according to [pattern].
 */
class PhoneVisualTransformation(
    private val pattern: String = "+X (XXX) XXX-XXXX",
    private val maskChar: Char = 'X'
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text.filter { it.isDigit() }
        val formatted = AuraInputFormatters.formatPhone(raw, pattern, maskChar)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var orig = 0
                var trans = 0
                while (orig < offset && trans < formatted.length) {
                    if (pattern.getOrNull(trans) == maskChar) {
                        orig++
                    }
                    trans++
                }
                return trans.coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                var orig = 0
                var trans = 0
                while (trans < offset && trans < pattern.length) {
                    if (pattern[trans] == maskChar) {
                        orig++
                    }
                    trans++
                }
                return orig.coerceAtMost(raw.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}
