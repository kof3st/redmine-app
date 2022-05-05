package me.kofesst.android.redminecomposeapp.feature.data.model.issue

import me.kofesst.android.redminecomposeapp.feature.data.model.CustomField

data class CreateIssueDetails(
    val project_id: Int,
    val subject: String,
    val description: String?,
    val assigned_to_id: Int?,
    val priority_id: Int,
    val tracker_id: Int,
    val status_id: Int = 1,
    val custom_fields: MutableList<CustomField> = mutableListOf(),
    val notes: String? = null
) {
    fun addCustomField(field: CustomField) {
        custom_fields.add(field)
    }
}