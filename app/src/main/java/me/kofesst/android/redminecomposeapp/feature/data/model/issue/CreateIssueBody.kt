package me.kofesst.android.redminecomposeapp.feature.data.model.issue

import com.tickaroo.tikxml.annotation.*
import me.kofesst.android.redminecomposeapp.feature.data.model.CustomField

@Xml(name = "issue")
class CreateIssueBody(
    @PropertyElement
    val project_id: Int,

    @PropertyElement
    val subject: String,

    @PropertyElement
    val description: String,

    @PropertyElement
    val assigned_to_id: String,

    @PropertyElement
    val priority_id: Int,

    @PropertyElement
    val tracker_id: Int,

    @PropertyElement
    val status_id: Int = 1,

    @Path("custom_fields")
    @Element
    val custom_fields: MutableList<CustomField> = mutableListOf(),

    @Path("custom_fields")
    @Attribute(name = "type")
    val custom_fields_type: String = "array",

    @PropertyElement
    val notes: String? = null,
) {
    fun addCustomField(field: CustomField) {
        custom_fields.add(field)
    }
}