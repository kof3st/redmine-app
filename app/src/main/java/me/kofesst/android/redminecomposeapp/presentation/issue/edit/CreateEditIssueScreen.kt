package me.kofesst.android.redminecomposeapp.presentation.issue.edit

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.util.formatDate
import me.kofesst.android.redminecomposeapp.domain.util.parseDeadlineString
import me.kofesst.android.redminecomposeapp.presentation.LocalAppState
import me.kofesst.android.redminecomposeapp.presentation.util.LoadingHandler
import me.kofesst.android.redminecomposeapp.presentation.util.LoadingResult
import me.kofesst.android.redminecomposeapp.presentation.util.ValidationEvent
import me.kofesst.android.redminecomposeapp.ui.component.DropdownItem
import me.kofesst.android.redminecomposeapp.ui.component.RedmineCard
import me.kofesst.android.redminecomposeapp.ui.component.RedmineDropdown
import me.kofesst.android.redminecomposeapp.ui.component.RedmineTextField
import java.util.*

@Composable
fun CreateEditIssueScreen(
    issueId: Int,
    projectId: Int,
    viewModel: CreateEditIssueViewModel = hiltViewModel(),
) {
    val appState = LocalAppState.current
    val navController = appState.navController

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
    LoadingHandler(loadingState, appState.scaffoldState.snackbarHostState)

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
            AttachmentsSection(
                formState = formState,
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
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
fun AttachmentsSection(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val selectFileRequester = rememberFilePicker {
        val contentUri = it.data ?: return@rememberFilePicker
        if (contentUri.path == null) return@rememberFilePicker

        val file = FileUtils.getFile(context, contentUri) ?: return@rememberFilePicker
        val type = getFileMimeType(file.path)

        viewModel.onFormEvent(
            IssueFormEvent.AttachmentAdded(
                attachment = FileData(
                    file = file,
                    type = type
                )
            )
        )
    }

    val storageRequester = rememberPermissionLauncher {
        selectFileRequester.launch(filePickerIntent)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        formState.attachments.forEach { attachment ->
            AttachmentItem(
                attachment = attachment,
                onDeleteClick = {
                    viewModel.onFormEvent(
                        IssueFormEvent.AttachmentRemoved(attachment)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    TextButton(
        onClick = {
            if (hasStoragePermission) {
                selectFileRequester.launch(filePickerIntent)
            } else {
                storageRequester.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Добавить вложение",
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun AttachmentItem(
    attachment: FileData,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {},
) {
    RedmineCard(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(13.dp)
        ) {
            Text(
                text = attachment.file.name,
                style = MaterialTheme.typography.body1
            )
            TextButton(onClick = onDeleteClick) {
                Text(
                    text = "Открепить",
                    style = MaterialTheme.typography.button,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SubjectField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel,
) {
    RedmineTextField(
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
    trackers: List<IdName>,
) {
    RedmineDropdown(
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
    priorities: List<IdName>,
) {
    RedmineDropdown(
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
    members: List<IdName>,
) {
    RedmineDropdown(
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
        hasNullValue = true,
        onNullValueSelected = {
            viewModel.onFormEvent(IssueFormEvent.AssignedToChanged(null))
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun StatusesField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel,
    statuses: List<IdName>,
) {
    RedmineDropdown(
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

@Composable
fun DeadlineField(
    formState: IssueFormState,
    viewModel: CreateEditIssueViewModel,
) {
    val context = LocalContext.current

    val localFocusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        RedmineTextField(
            value = formState.deadline?.formatDate() ?: "",
            onValueChange = { value ->
                val date = value.parseDeadlineString()
                viewModel.onFormEvent(
                    IssueFormEvent.DeadlineChanged(
                        deadline = date
                    )
                )
            },
            isReadOnly = true,
            label = "Дедлайн",
            leadingIcon = painterResource(R.drawable.ic_date_24),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.hasFocus) {
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
                    }
                    localFocusManager.clearFocus(force = true)
                }
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

fun showDatePicker(
    context: Context,
    selected: Date? = null,
    onDateSelected: (Date) -> Unit = {},
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
    viewModel: CreateEditIssueViewModel,
) {
    RedmineTextField(
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
    viewModel: CreateEditIssueViewModel,
) {
    RedmineTextField(
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