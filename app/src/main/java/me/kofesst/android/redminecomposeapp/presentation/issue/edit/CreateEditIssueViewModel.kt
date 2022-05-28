package me.kofesst.android.redminecomposeapp.presentation.issue.edit

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.kofesst.android.redminecomposeapp.data.model.CustomFieldDto
import me.kofesst.android.redminecomposeapp.data.model.attachment.UploadDataDto
import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.model.UploadData
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.domain.util.UserHolder
import me.kofesst.android.redminecomposeapp.domain.util.ValidationResult
import me.kofesst.android.redminecomposeapp.presentation.ViewModelBase
import me.kofesst.android.redminecomposeapp.presentation.util.ValidationEvent
import javax.inject.Inject

@HiltViewModel
class CreateEditIssueViewModel @Inject constructor(
    private val useCases: UseCases,
    private val userHolder: UserHolder,
) : ViewModelBase() {
    private var projectId: Int = -1

    private val _priorities = MutableStateFlow(IdName.priorities)
    val priorities get() = _priorities.asStateFlow()

    private val _trackers = MutableStateFlow<List<IdName>>(listOf())
    val trackers get() = _trackers.asStateFlow()

    private val _members = MutableStateFlow<List<IdName>>(listOf())
    val members get() = _members.asStateFlow()

    private val _statuses = MutableStateFlow<List<IdName>>(listOf())
    val statuses get() = _statuses.asStateFlow()

    private val _editing = mutableStateOf<Issue?>(null)
    val editing: State<Issue?> get() = _editing

    var formState by mutableStateOf(IssueFormState())

    private val validationChannel = Channel<ValidationEvent>()
    val validationEvents = validationChannel.receiveAsFlow()

    fun loadDetails(issueId: Int, projectId: Int) {
        this.projectId = projectId

        startLoading {
            _trackers.value = useCases.getTrackers()
            _statuses.value = useCases.getStatuses()

            if (issueId != -1) {
                _editing.value = useCases.getIssueDetails(issueId).also {
                    this.projectId = it.projectId

                    formState = IssueFormState(
                        subject = it.subject,
                        tracker = it.tracker,
                        priority = it.priority,
                        assignedTo = it.assignedTo?.let { assignedTo ->
                            IdName(
                                id = assignedTo.id,
                                name = assignedTo.name
                            )
                        },
                        status = it.status,
                        deadline = it.deadline,
                        description = it.description
                    )
                }
            }

            _members.value = try {
                useCases.getMembers(this.projectId).map { it.user }
            } catch (e: Exception) {
                listOf(
                    IdName(
                        id = userHolder.requireUser().id,
                        name = "Я"
                    )
                )
            }
        }
    }

    fun onFormEvent(event: IssueFormEvent) {
        when (event) {
            is IssueFormEvent.SubjectChanged -> {
                formState = formState.copy(subject = event.subject)
            }
            is IssueFormEvent.TrackerChanged -> {
                formState = formState.copy(tracker = event.tracker)
            }
            is IssueFormEvent.PriorityChanged -> {
                formState = formState.copy(priority = event.priority)
            }
            is IssueFormEvent.AssignedToChanged -> {
                formState = formState.copy(assignedTo = event.assignedTo)
            }
            is IssueFormEvent.StatusChanged -> {
                formState = formState.copy(status = event.status)
            }
            is IssueFormEvent.DeadlineChanged -> {
                formState = formState.copy(deadline = event.deadline)
            }
            is IssueFormEvent.DescriptionChanged -> {
                formState = formState.copy(description = event.description)
            }
            is IssueFormEvent.ChangesNotesChanged -> {
                formState = formState.copy(changesNotes = event.notes)
            }
            is IssueFormEvent.AttachmentAdded -> {
                formState = formState.copy(
                    attachments = (formState.attachments + event.attachment).toMutableList()
                )
            }
            is IssueFormEvent.AttachmentRemoved -> {
                formState = formState.copy(
                    attachments = (formState.attachments - event.attachment).toMutableList()
                )
            }
            is IssueFormEvent.Submit -> {
                onDataSubmit()
            }
        }
    }

    private fun onDataSubmit() {
        val subjectResult = useCases.validateForEmptyField(formState.subject)
        val trackerResult = useCases.validateForNotNullField(formState.tracker)
        val priorityResult = useCases.validateForNotNullField(formState.priority)
        val statusResult = if (editing.value != null) {
            useCases.validateForNotNullField(formState.status)
        } else {
            ValidationResult(true)
        }

        formState = formState.copy(
            subjectError = subjectResult.errorMessage,
            trackerError = trackerResult.errorMessage,
            priorityError = priorityResult.errorMessage,
            statusError = statusResult.errorMessage
        )

        val hasError = listOf(
            subjectResult,
            trackerResult,
            priorityResult
        ).any { !it.isSuccessful }

        if (!hasError) {
            startLoading(
                onSuccessCallback = {
                    viewModelScope.launch {
                        validationChannel.send(ValidationEvent.Success)
                    }
                }
            ) {
                val uploads = formState.attachments.map {
                    UploadDataDto(
                        token = useCases.uploadFile(it.file, it.type),
                        fileName = it.file.name,
                        type = it.type
                    )
                }

                val issue = Issue(
                    projectId = projectId,
                    subject = formState.subject,
                    description = formState.description ?: "",
                    assignedTo = formState.assignedTo,
                    priority = formState.priority!!,
                    tracker = formState.tracker!!,
                    status = formState.status ?: IdName(1, ""),
                    customFields = listOf(
                        CustomFieldDto.getDeadline(formState.deadline).toCustomField()
                    )
                )

                editing.value?.run {
                    useCases.updateIssue(
                        issueId = this.id,
                        issue = issue,
                        attachments = uploads.map {
                            UploadData(
                                token = it.token,
                                fileName = it.fileName,
                                type = it.type
                            )
                        },
                        notes = formState.changesNotes
                    )
                } ?: kotlin.run {
                    useCases.createIssue(issue, uploads.map {
                        UploadData(
                            token = it.token,
                            fileName = it.fileName,
                            type = it.type
                        )
                    })
                }
            }
        }
    }
}