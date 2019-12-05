package com.molo17.commons.android

import android.text.format.DateUtils
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Created by Damiano Giusti on 2019-05-20.
 */
interface RelativeTimeFormatter {
    fun toRelativeString(date: Date, now: Long): String
    fun toDecimalTimeString(time: Long): String
}

private const val FRACTION_SECONDS = 60
private const val FRACTION_MINUTES = FRACTION_SECONDS * 60
private const val FRACTION_HOURS = FRACTION_MINUTES * 24
private const val FRACTION_DAYS = FRACTION_HOURS * 30.4
private const val FRACTION_MONTHS = FRACTION_DAYS * 12

class AndroidRelativeTimeFormatter @Inject constructor() : RelativeTimeFormatter {
    override fun toRelativeString(date: Date, now: Long): String =
        DateUtils.getRelativeTimeSpanString(date.time, now, DateUtils.SECOND_IN_MILLIS).toString()

    override fun toDecimalTimeString(time: Long): String {
        return toDecimalTimeString(time, 0).trim()
    }

    private fun toDecimalTimeString(time: Long?, iteration: Int): String {
        return when {
            iteration == 2 -> ""
            time == null -> "" // Null when the decimal part obtained in a previous iteration is zero.
            time == 0L -> "0s"
            time < FRACTION_SECONDS -> "${time}s"
            time < FRACTION_MINUTES -> "${time / FRACTION_SECONDS}m " + toDecimalTimeString(takeIfNotZero(time % FRACTION_SECONDS), iteration + 1)
            time < FRACTION_HOURS -> "${time / FRACTION_MINUTES}h " + toDecimalTimeString(takeIfNotZero(time % FRACTION_MINUTES), iteration + 1)
            time < FRACTION_DAYS -> "${time / FRACTION_HOURS}d " + toDecimalTimeString(takeIfNotZero(time % FRACTION_HOURS), iteration + 1)
            time < FRACTION_MONTHS -> "${(time / FRACTION_DAYS).roundToInt()}mo " + toDecimalTimeString(takeIfNotZero(time % FRACTION_DAYS.roundToInt()), iteration + 1)
            else -> "${(time / FRACTION_MONTHS).roundToInt()}y " + toDecimalTimeString(takeIfNotZero(time % FRACTION_MONTHS.roundToInt()), iteration + 1)
        }
    }

    private fun takeIfNotZero(long: Long): Long? = long.takeIf { it != 0L }
}