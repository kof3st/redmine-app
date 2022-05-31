package me.kofesst.android.redminecomposeapp.data.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import me.kofesst.android.redminecomposeapp.domain.model.CustomField
import me.kofesst.android.redminecomposeapp.domain.util.format
import java.util.*

@Xml(name = "custom_field")
data class CustomFieldDto(
    @Attribute
    val id: Int,

    @Attribute
    val name: String,

    @PropertyElement
    val value: String?,
) {
    companion object {
        fun fromCustomField(customField: CustomField) = CustomFieldDto(
            id = customField.id,
            name = customField.name,
            value = customField.value
        )

        fun getDeadline(date: Date?) = CustomFieldDto(
            id = 10,
            name = "Deadline",
            value = date?.format("yyyy-MM-dd") ?: ""
        )
    }

    fun toCustomField() = CustomField(
        id = id,
        name = name,
        value = value
    )
}