package me.kofesst.android.redminecomposeapp.feature.presentation.issue.edit

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Priority
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Tracker
import me.kofesst.android.redminecomposeapp.feature.data.model.membership.User
import me.kofesst.android.redminecomposeapp.feature.data.model.status.Status
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult
import me.kofesst.android.redminecomposeapp.feature.domain.util.ValidationEvent
import me.kofesst.android.redminecomposeapp.feature.domain.util.formatDate
import me.kofesst.android.redminecomposeapp.feature.domain.util.formatDeadlineString
import me.kofesst.android.redminecomposeapp.feature.presentation.*
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CreateEditIssueScreen(
    issueId: Int,
    projectId: Int,
    navController: NavController,
    viewModel: CreateEditIssueViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.loadDetails(issueId, projectId)
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    navController.navigateUp()
                }
            }
        }
    }

    val loadingState by viewModel.loadingState
    val isLoading = loadingState.state == LoadingResult.State.RUNNING
    if (isLoading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }

    val priorities by viewModel.priorities.collectAsState()
    val trackers by viewModel.trackers.collectAsState()
    val members by viewModel.members.collectAsState()
    val statuses by viewModel.statuses.collectAsState()

    val formState = viewModel.formState

    val editing by viewModel.editing
    val isEditing = editing != null

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SubjectField(
                formState = formState,
                viewModel = viewModel
            )
            TrackersField(
                formState = formState,
                viewModel = viewModel,
                trackers = trackers
            )
            PrioritiesField(
                formState = formState,
                viewModel = viewModel,
                priorities = priorities
            )
            AssignedToField(
                formState = formState,
                viewModel = viewModel,
                members = members
            )
            if (isEditing) {
                StatusesField(
                    formState = formState,
                    viewModel = viewModel,
                    statuses = statuses
                )
            }
            DeadlineField(
                formState = formState,
                viewModel = viewModel
            )
            DescriptionField(
                formState = formState,
                viewModel = viewModel
            )
            if (isEditing) {
                NotesField(
                    formState = formState,
                    viewModel = viewModel
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.onFormEvent(IssueFormEvent.Submit)
                },
                text = {
                    Text(
                        text = "Сохранить",
                        style = MaterialTheme.typography.body1
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SubjectField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel
) {
    OutlinedValidatedTextField(
        value = formState.subject,
        onValueChange = { viewModel.onFormEvent(IssueFormEvent.SubjectChanged(it)) },
        errorMessage = formState.subjectError,
        label = "Название",
        leadingIcon = painterResource(R.drawable.ic_short_text_24),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TrackersField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel,
    trackers: List<Tracker>
) {
    ValidatedDropdown(
        items = trackers.map {
            DropdownItem(
                text = it.name,
                onSelected = {
                    viewModel.onFormEvent(IssueFormEvent.TrackerChanged(it))
                }
            )
        },
        value = formState.tracker?.name ?: "",
        placeholder = "Трекер",
        errorMessage = formState.trackerError,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PrioritiesField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel,
    priorities: List<Priority>
) {
    ValidatedDropdown(
        items = priorities.map {
            DropdownItem(
                text = it.name,
                onSelected = {
                    viewModel.onFormEvent(IssueFormEvent.PriorityChanged(it))
                }
            )
        },
        value = formState.priority?.name ?: "",
        placeholder = "Приоритет",
        errorMessage = formState.priorityError,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AssignedToField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel,
    members: List<User>
) {
    Dropdown(
        items = members.map {
            DropdownItem(
                text = it.name,
                onSelected = {
                    viewModel.onFormEvent(IssueFormEvent.AssignedToChanged(it))
                }
            )
        },
        value = formState.assignedTo?.name ?: "",
        placeholder = "Исполнитель",
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun StatusesField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel,
    statuses: List<Status>
) {
    ValidatedDropdown(
        items = statuses.map {
            DropdownItem(
                text = it.name,
                onSelected = {
                    viewModel.onFormEvent(IssueFormEvent.StatusChanged(it))
                }
            )
        },
        value = formState.status?.name ?: "",
        placeholder = "Статус",
        errorMessage = formState.statusError,
        modifier = Modifier.fillMaxWidth()
    )
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DeadlineField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel
) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        DefaultTextField(
            value = formState.deadline?.formatDate() ?: "",
            onValueChange = { value ->
                val date = value.formatDeadlineString()
                viewModel.onFormEvent(
                    IssueFormEvent.DeadlineChanged(
                        deadline = date
                    )
                )
            },
            isReadOnly = true,
            label = "Дедлайн",
            leadingIcon = painterResource(R.drawable.ic_date_24),
            trailingIcon = painterResource(R.drawable.ic_more_horiz_24),
            onTrailingIconClick = {
                showDatePicker(
                    context = context,
                    selected = formState.deadline
                ) { deadline ->
                    viewModel.onFormEvent(
                        IssueFormEvent.DeadlineChanged(
                            deadline = deadline
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        formState.deadline?.run {
            TextButton(
                onClick = {
                    viewModel.onFormEvent(
                        IssueFormEvent.DeadlineChanged(
                            deadline = null
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Удалить",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun showDatePicker(
    context: Context,
    selected: Date? = null,
    onDateSelected: (Date) -> Unit = {}
) {
    val date = Calendar.getInstance()
    date.time = selected ?: Date()

    DatePickerDialog(
        context,
        { _, year, month, day ->
            date.set(Calendar.YEAR, year)
            date.set(Calendar.MONTH, month)
            date.set(Calendar.DAY_OF_MONTH, day)
            onDateSelected(date.time)
        },
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH),
        date.get(Calendar.DAY_OF_MONTH)
    ).show()
}

@Composable
fun DescriptionField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel
) {
    DefaultTextField(
        value = formState.description ?: "",
        onValueChange = { value ->
            viewModel.onFormEvent(
                IssueFormEvent.DescriptionChanged(
                    description = value.ifBlank { null }
                )
            )
        },
        label = "Описание",
        leadingIcon = painterResource(R.drawable.ic_description_24),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NotesField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel
) {
    DefaultTextField(
        value = formState.changesNotes ?: "",
        onValueChange = { value ->
            viewModel.onFormEvent(
                IssueFormEvent.ChangesNotesChanged(
                    notes = value.ifBlank { null }
                )
            )
        },
        label = "Описание изменений",
        leadingIcon = painterResource(R.drawable.ic_notes_24),
        modifier = Modifier.fillMaxWidth()
    )
}