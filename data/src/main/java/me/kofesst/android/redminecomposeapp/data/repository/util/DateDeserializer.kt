package me.kofesst.android.redminecomposeapp.data.repository.util

import java.util.*

class DateDeserializer : CustomDateDeserializer<Date>(
    dateFormat = "yyyy/MM/dd"
) {
    override fun getFromDate(date: Date): Date {
        return date
    }
}