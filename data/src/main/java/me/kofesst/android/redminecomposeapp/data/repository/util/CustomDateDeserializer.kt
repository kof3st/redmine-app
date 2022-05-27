package me.kofesst.android.redminecomposeapp.data.repository.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

abstract class CustomDateDeserializer<T : Date>(
    dateFormat: String
) : JsonDeserializer<T> {
    private val format = SimpleDateFormat(dateFormat, Locale.ROOT)

    protected abstract fun getFromDate(date: Date): T

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): T {
        return getFromDate(
            try {
                format.parse(json!!.asString)!!
            } catch (e: Exception) {
                e.printStackTrace()
                Date()
            }
        )
    }
}