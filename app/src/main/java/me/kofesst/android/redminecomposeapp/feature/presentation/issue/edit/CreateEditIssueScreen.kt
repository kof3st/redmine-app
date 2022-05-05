package me.kofesst.android.redminecomposeapp.feature.presentation.issue.edit

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult
import me.kofesst.android.redminecomposeapp.feature.domain.util.ValidationEvent
import me.kofesst.android.redminecomposeapp.feature.presentation.*

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
                    navController.navigate(
                        if (issueId != -1) {
                            Screen.Issue.withArgs("issueId" to issueId)
                        } else {
                            Screen.Project.withArgs("projectId" to projectId)
                        }
                    )
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
            OutlinedValidatedTextField(
                value = formState.subject,
                onValueChange = { viewModel.onFormEvent(IssueFormEvent.SubjectChanged(it)) },
                errorMessage = formState.subjectError,
                label = "Название",
                leadingIcon = painterResource(R.drawable.ic_short_text_24),
                modifier = Modifier.fillMaxWidth()
            )
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
            if (isEditing) {
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
            if (isEditing) {
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