package com.molo17.commons.android

import android.text.InputFilter
import android.text.Spanned

/**
 * Input filter which rejects text which represents a number that is not into the given int range.
 *
 * Created by Damiano Giusti on 08/02/18.
 */
class NumberRangeInputFilter(
    private val minValue: Double,
    private val maxValue: Double,
    private val maxAllowedDecimalCount: Int
) : InputFilter {

    @Suppress("ConvertTwoComparisonsToRangeCheck")
    override fun filter(
        source: CharSequence?, // Will contain the inserted char.
        start: Int,
        end: Int,
        dest: Spanned?, // Will contain the actual text without the inserted char.
        dstart: Int,
        dend: Int
    ): CharSequence? {
        // Create a string joining the newly inserted char.
        val string = dest?.toString() + source
        val double = string.toDoubleOrNull() ?: return ""

        if (double >= minValue && double <= maxValue) {
            if (maxAllowedDecimalCount > 0) {
                val dotIndex = string.indexOf(".").takeUnless { it == -1 }
                // If the string contains a dot, the user started inserting the decimal part of the number.
                if (dotIndex != null && string.substring(dotIndex + 1).count() > maxAllowedDecimalCount) {
                    // If the decimal part of the number has a number of digits greater than the
                    // allowed, then block the current insertion.
                    return ""
                } else {
                    // The insertion of the decimal position is valid.
                    return null
                }
            } else {
                if (source?.contains(".") == true) {
                    // If the user inserted a dot and no decimal digits are allowed,
                    // block the current insertion.
                    return ""
                } else {
                    // The insertion of the char at current position is valid.
                    return null
                }
            }
        } else {
            // The Double value is not in range, so block the current insertion.
            return ""
        }
    }
}