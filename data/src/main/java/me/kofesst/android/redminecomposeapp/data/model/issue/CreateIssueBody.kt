package me.kofesst.android.redminecomposeapp.data.model.issue

import com.tickaroo.tikxml.annotation.*
import me.kofesst.android.redminecomposeapp.data.model.CustomFieldDto
import me.kofesst.android.redminecomposeapp.data.model.attachment.UploadDataDto
import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.model.UploadData

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

    @Path("uploads")
    @Element
    val uploads: List<UploadDataDto>,

    @Path("uploads")
    @Attribute(name = "type")
    val uploads_type: String = "array",

    @Path("custom_fields")
    @Element
    val custom_fields: MutableList<CustomFieldDto> = mutableListOf(),

    @Path("custom_fields")
    @Attribute(name = "type")
    val custom_fields_type: String = "array",

    @PropertyElement
    val notes: String? = null,
) {
    companion object {
        fun fromIssue(
            issue: Issue,
            attachments: List<UploadData>,
            notes: String? = null,
        ) = CreateIssueBody(
            project_id = issue.projectId,
            subject = issue.subject,
            description = issue.description ?: "",
            assigned_to_id = issue.assignedTo?.id?.toString() ?: "",
            priority_id = issue.priority.id,
            tracker_id = issue.tracker.id,
            status_id = issue.status.id,
            uploads = attachments.map {
                UploadDataDto(
                    token = it.token,
                    fileName = it.fileName,
                    type = it.type
                )
            },
            custom_fields = issue.customFields.map {
                CustomFieldDto.fromCustomField(it)
            }.toMutableList(),
            notes = notes
        )
    }

    fun addCustomField(field: CustomFieldDto) {
        custom_fields.add(field)
    }
}