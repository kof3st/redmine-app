package me.kofesst.android.redminecomposeapp.feature.data.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import me.kofesst.android.redminecomposeapp.feature.domain.util.format
import java.util.*

@Xml(name = "custom_field")
data class CustomField(
    @Attribute
    val id: Int,

    @Attribute
    val name: String,

    @PropertyElement
    val value: String?,
) {
    companion object {
        fun getDeadline(date: Date?) = CustomField(
            id = 10,
            name = "Deadline",
            value = date?.format("yyyy-MM-dd") ?: ""
        )
    }
}