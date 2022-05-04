package me.kofesst.android.redminecomposeapp.feature.domain.util

import java.text.DecimalFormat
import java.util.*

fun Double.formatHours(): String {
    val formatter = DecimalFormat()
    formatter.maximumFractionDigits = 1
    formatter.minimumFractionDigits = 0

    val intPart = this.toInt()
    val suffix: String = when {
        intPart == 1 && this > 1.0 -> {
            "часа" // 1.3 часа
        }
        intPart in 10..19 -> {
            "часов" // 15 часов
        }
        else -> {
            when (intPart % 10) {
                1 -> "час"
                in 2..4 -> "часа" // 22 часа
                else -> "часов" // 25 часов
            }
        }
    }

    return "${formatter.format(this)} $suffix"
}

fun Date.formatDate(showTime: Boolean = false): String {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val monthName = calendar.getMonth()
    val year = calendar.get(Calendar.YEAR)

    var formatted = "%d %s %d".format(dayOfMonth, monthName, year)
    if (showTime) {
        val hours = calendar.get(Calendar.HOUR_OF_DAY).toString()
        val minutes = calendar.get(Calendar.MINUTE).toString()
        formatted = "%s в %s:%s".format(
            formatted,
            hours.padStart(2, '0'),
            minutes.padStart(2, '0')
        )
    }

    return formatted
}

fun Calendar.getMonth(): String {
    return when (this.get(Calendar.MONTH)) {
        0 -> "янв"
        1 -> "фев"
        2 -> "мар"
        3 -> "апр"
        4 -> "мая"
        5 -> "июн"
        6 -> "июл"
        7 -> "авг"
        8 -> "сен"
        9 -> "окт"
        10 -> "ноя"
        else -> "дек"
    }
}