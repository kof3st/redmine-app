package me.kofesst.android.redminecomposeapp.presentation.issue.edit

import me.kofesst.android.redminecomposeapp.domain.model.IdName
import java.util.*

data class IssueFormState(
    val subject: String = "",
    val subjectError: String? = null,
    val tracker: IdName? = null,
    val trackerError: String? = null,
    val priority: IdName? = null,
    val priorityError: String? = null,
    val assignedTo: IdName? = null,
    val status: IdName? = null,
    val statusError: String? = null,
    val deadline: Date? = null,
    val description: String? = null,
    val changesNotes: String? = null,
)