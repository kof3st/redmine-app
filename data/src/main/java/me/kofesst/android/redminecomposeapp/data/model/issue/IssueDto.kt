package me.kofesst.android.redminecomposeapp.data.model.issue

import me.kofesst.android.redminecomposeapp.data.model.CustomFieldDto
import me.kofesst.android.redminecomposeapp.data.model.attachment.AttachmentDto
import me.kofesst.android.redminecomposeapp.data.model.journal.JournalDto
import me.kofesst.android.redminecomposeapp.data.model.status.StatusDto
import me.kofesst.android.redminecomposeapp.data.repository.util.DateTime
import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.model.Issue
import java.text.SimpleDateFormat
import java.util.*

data class IssueDto(
    val assigned_to: AssignedToDto?,
    val attachments: List<AttachmentDto>? = null,
    val author: AuthorDto,
    val children: List<ChildIssueDto>? = null,
    val created_on: DateTime,
    val custom_fields: List<CustomFieldDto>,
    var description: String?,
    val done_ratio: Int,
    var due_date: Date?,
    val journals: List<JournalDto>? = null,
    val estimated_hours: Double,
    val id: Int,
    val parent: ParentDto?,
    var priority: PriorityDto,
    val project: ProjectDto,
    val spent_hours: Double,
    val start_date: Date,
    val status: StatusDto,
    var subject: String,
    var tracker: TrackerDto,
    val updated_on: DateTime,
) {
    val deadline
        get(): Date? {
            val customField = custom_fields.firstOrNull { customField ->
                customField.id == 10
            } ?: return null

            if (customField.value?.isBlank() != false) return null

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
            return format.parse(customField.value) ?: Date(0)
        }

    fun toIssue() = Issue(
        id = id,
        projectId = project.id,
        subject = subject,
        description = description,
        status = IdName(status.id, status.name),
        tracker = IdName(tracker.id, tracker.name),
        priority = IdName(priority.id, priority.name),
        author = IdName(author.id, author.name),
        assignedTo = assigned_to?.let { IdName(assigned_to.id, assigned_to.name) },
        doneRatio = done_ratio,
        estimatedHours = estimated_hours,
        spentHours = spent_hours,
        createdOn = created_on,
        updatedOn = updated_on,
        dueDate = due_date,
        deadline = deadline,
        journals = journals?.map { it.toJournal() } ?: listOf(),
        attachments = attachments?.map { it.toAttachment() } ?: listOf(),
        children = children?.map { it.toIdName() } ?: listOf(),
        customFields = custom_fields.map { it.toCustomField() }
    )
}