package me.kofesst.android.redminecomposeapp.data.repository.util

import java.util.*

class DateTimeDeserializer : CustomDateDeserializer<DateTime>(
    dateFormat = "yyyy/MM/dd HH:mm:ss Z"
) {
    override fun getFromDate(date: Date): DateTime {
        return DateTime(date)
    }
}