package me.kofesst.android.redminecomposeapp.feature.data.repository.util

import java.util.*

class DateDeserializer : CustomDateDeserializer<Date>(
    dateFormat = "yyyy/MM/dd HH:mm:ss Z"
) {
    override fun getFromDate(date: Date): Date {
        return date
    }
}