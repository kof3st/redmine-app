package me.kofesst.android.redminecomposeapp.presentation.issue.edit

import me.kofesst.android.redminecomposeapp.domain.model.IdName
import java.util.*

sealed class IssueFormEvent {
    data class SubjectChanged(val subject: String) : IssueFormEvent()
    data class TrackerChanged(val tracker: IdName) : IssueFormEvent()
    data class PriorityChanged(val priority: IdName) : IssueFormEvent()
    data class AssignedToChanged(val assignedTo: IdName?) : IssueFormEvent()
    data class StatusChanged(val status: IdName) : IssueFormEvent()
    data class DeadlineChanged(val deadline: Date?) : IssueFormEvent()
    data class DescriptionChanged(val description: String?) : IssueFormEvent()
    data class ChangesNotesChanged(val notes: String?) : IssueFormEvent()
    object Submit : IssueFormEvent()
}