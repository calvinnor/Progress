@file: JvmName("DateTimeUtils")

package com.calvinnor.progress.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

private const val TIME_FORMATTER = "%02d:%02d %s"
private const val DATE_FORMATTER = "%02d-%02d-%02d"
private const val TASK_DATE_FORMATTER = "dd MMM"

private val dateFormatter = SimpleDateFormat(TASK_DATE_FORMATTER, Locale.ENGLISH)

/**
 * Returns a human-readable Time.
 * eg: 5:20 AM
 */
fun getFormattedTime(hourOfDay: Int, minute: Int): String {
    val timeSet: String
    var hour = hourOfDay

    if (hourOfDay > 12) {
        hour -= 12
        timeSet = "PM"
    } else if (hour == 0) {
        hour += 12
        timeSet = "AM"
    } else if (hour == 12) {
        timeSet = "PM"
    } else {
        timeSet = "AM"
    }

    return String.format(Locale.ENGLISH, TIME_FORMATTER, hour, minute, timeSet)
}

fun getFormattedTime(calendar: Calendar) =
        getFormattedTime(calendar.get(HOUR_OF_DAY), calendar.get(MINUTE))


/**
 * Returns a human-readable date.
 * eg: 05-07-2018
 */
fun getFormattedDate(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
    val shortenedYear = year % 100 // Use only last 2 digits
    val displayMonth = monthOfYear + 1 // Months range from 0 - 11
    return String.format(Locale.ENGLISH, DATE_FORMATTER, dayOfMonth, displayMonth, shortenedYear)
}

fun getFormattedDate(calendar: Calendar) =
        getFormattedDate(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH))

fun getFormattedDateForList(calendar: Calendar) =
        dateFormatter.format(calendar.time)
