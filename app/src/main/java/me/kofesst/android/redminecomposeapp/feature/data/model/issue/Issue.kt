package me.kofesst.android.redminecomposeapp.feature.data.model.issue

import me.kofesst.android.redminecomposeapp.feature.data.model.journal.Journal
import me.kofesst.android.redminecomposeapp.feature.data.model.CustomField
import me.kofesst.android.redminecomposeapp.feature.data.model.attachment.Attachment
import me.kofesst.android.redminecomposeapp.feature.data.model.status.Status
import me.kofesst.android.redminecomposeapp.feature.data.repository.util.DateTime
import java.util.*

data class Issue(
    val assigned_to: AssignedTo?,
    val attachments: List<Attachment> = listOf(),
    val author: Author,
    val children: List<ChildIssue>? = null,
    val created_on: DateTime,
    val custom_fields: List<CustomField>,
    var description: String?,
    val done_ratio: Int,
    var due_date: Date?,
    val journals: List<Journal> = listOf(),
    val estimated_hours: Double,
    val id: Int,
    val parent: Parent?,
    var priority: Priority,
    val project: Project,
    val start_date: Date,
    val status: Status,
    var subject: String,
    var tracker: Tracker,
    val updated_on: DateTime
)