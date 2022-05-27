package me.kofesst.android.redminecomposeapp.domain.model

import me.kofesst.android.redminecomposeapp.domain.model.journal.Journal
import java.util.*

data class Issue(
    val id: Int = 0,
    val projectId: Int = 0,
    var subject: String = "",
    var description: String? = null,
    var status: IdName = IdName(0, ""),
    var tracker: IdName = IdName(0, ""),
    var priority: IdName = IdName(0, ""),
    val author: IdName = IdName(0, ""),
    val assignedTo: IdName? = null,
    val doneRatio: Int = 0,
    val estimatedHours: Double = 0.0,
    val spentHours: Double = 0.0,
    val createdOn: Date = Date(),
    val updatedOn: Date = Date(),
    val dueDate: Date? = null,
    val deadline: Date? = null,
    val journals: List<Journal> = listOf(),
    val attachments: List<Attachment> = listOf(),
    val children: List<IdName> = listOf(),
    val customFields: List<CustomField> = listOf(),
)