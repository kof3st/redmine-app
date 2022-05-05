package me.kofesst.android.redminecomposeapp.feature.presentation.issue.edit

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Priority
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Tracker
import me.kofesst.android.redminecomposeapp.feature.data.model.membership.User
import me.kofesst.android.redminecomposeapp.feature.data.model.status.Status
import java.util.*

data class IssueFormState(
    val subject: String = "",
    val subjectError: String? = null,
    val tracker: Tracker? = null,
    val trackerError: String? = null,
    val priority: Priority? = null,
    val priorityError: String? = null,
    val assignedTo: User? = null,
    val status: Status? = null,
    val statusError: String? = null,
    val deadline: Date? = null,
    val description: String? = null,
    val changesNotes: String? = null
)