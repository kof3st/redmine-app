package me.kofesst.android.redminecomposeapp.feature.presentation.issue.edit

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Priority
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Tracker
import me.kofesst.android.redminecomposeapp.feature.data.model.membership.User
import me.kofesst.android.redminecomposeapp.feature.data.model.status.Status
import java.util.*

sealed class IssueFormEvent {
    data class SubjectChanged(val subject: String) : IssueFormEvent()
    data class TrackerChanged(val tracker: Tracker) : IssueFormEvent()
    data class PriorityChanged(val priority: Priority) : IssueFormEvent()
    data class AssignedToChanged(val assignedTo: User?) : IssueFormEvent()
    data class StatusChanged(val status: Status) : IssueFormEvent()
    data class DeadlineChanged(val deadline: Date?) : IssueFormEvent()
    data class DescriptionChanged(val description: String?) : IssueFormEvent()
    data class ChangesNotesChanged(val notes: String?) : IssueFormEvent()
    object Submit : IssueFormEvent()
}