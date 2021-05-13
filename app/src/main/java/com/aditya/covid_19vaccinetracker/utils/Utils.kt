package com.aditya.covid_19vaccinetracker.utils

import java.util.*

const val NOTIFICATION_INTERVAL = 15 * 60 * 1000L
fun twoDigit(number: Int): String {
    return if (number < 10) "0$number" else "$number"
}

fun getFormattedDate(): String {
    val calendar = Calendar.getInstance()
    return "${twoDigit(calendar[Calendar.DAY_OF_MONTH])}-${twoDigit(calendar[Calendar.MONTH] + 1)}-${
        twoDigit(calendar[Calendar.YEAR])
    }"
}