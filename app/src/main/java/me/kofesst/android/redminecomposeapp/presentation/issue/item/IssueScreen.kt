package me.kofesst.android.redminecomposeapp.presentation.issue.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.domain.model.Attachment
import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.model.journal.Journal
import me.kofesst.android.redminecomposeapp.domain.util.*
import me.kofesst.android.redminecomposeapp.presentation.LocalAppState
import me.kofesst.android.redminecomposeapp.presentation.Screen
import me.kofesst.android.redminecomposeapp.presentation.util.LoadingHandler
import me.kofesst.android.redminecomposeapp.presentation.util.LoadingResult
import me.kofesst.android.redminecomposeapp.ui.component.RedmineCard
import me.kofesst.android.redminecomposeapp.ui.component.RedmineSwipeRefresh

@Composable
fun IssueScreen(
    issueId: Int,
    viewModel: IssueViewModel = hiltViewModel(),
) {
    val appState = LocalAppState.current
    val navController = appState.navController

    LaunchedEffect(key1 = true) {
        viewModel.refreshData(issueId)
    }

    val loadingState by viewModel.loadingState
    LoadingHandler(loadingState, appState.scaffoldState.snackbarHostState)

    val isLoading = loadingState.state == LoadingResult.State.RUNNING

    val issue by viewModel.issue.collectAsState()
    val statuses by viewModel.statuses.collectAsState()
    val priorities by viewModel.priorities.collectAsState()
    val trackers by viewModel.trackers.collectAsState()

    AnimatedVisibility(
        visible = issue != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        RedmineSwipeRefresh(
            refreshState = rememberSwipeRefreshState(isLoading),
            onRefresh = { viewModel.refreshData(issueId) },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    issue?.also { issue ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 20.dp)
                        ) {
                            HeaderSection(
                                issue = issue,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            )
                            Divider(
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .padding(horizontal = 20.dp)
                            )
                            DetailsSection(
                                issue = issue,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            )
                            if (issue.attachments.isNotEmpty()) {
                                Divider(
                                    modifier = Modifier
                                        .padding(vertical = 10.dp)
                                        .padding(horizontal = 20.dp)
                                )
                                AttachmentsSection(
                                    issue = issue,
                                    apiKey = viewModel.userHolder.apiKey,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            if (issue.children.isNotEmpty()) {
                                Divider(
                                    modifier = Modifier
                                        .padding(vertical = 10.dp)
                                        .padding(horizontal = 20.dp)
                                )
                                ChildrenSection(
                                    children = issue.children,
                                    navController = navController,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                )
                            }
                            if (issue.journals.isNotEmpty()) {
                                Divider(
                                    modifier = Modifier
                                        .padding(vertical = 10.dp)
                                        .padding(horizontal = 20.dp)
                                )
                                JournalsSection(
                                    issue = issue,
                                    statuses = statuses,
                                    priorities = priorities,
                                    trackers = trackers,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                )
                            }
                        }
                    }
                }
                FloatingActionButton(
                    onClick = {
                        navController.navigate(
                            Screen.CreateEditIssue.withArgs(
                                "issueId" to issueId
                            )
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit_24),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun JournalsSection(
    issue: Issue,
    statuses: List<IdName>,
    priorities: List<IdName>,
    trackers: List<IdName>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        issue.journals
            .sortedByDescending { it.id }
            .forEach { journal ->
                JournalItem(
                    journal,
                    statuses,
                    priorities,
                    trackers
                )
            }
    }
}

@Composable
fun JournalItem(
    journal: Journal,
    statuses: List<IdName>,
    priorities: List<IdName>,
    trackers: List<IdName>,
) {
    RedmineCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.changes_header, journal.id),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            if (journal.notes != null) {
                Text(
                    text = journal.notes!!,
                    style = MaterialTheme.typography.body1
                )
            }
            if (journal.details.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                journal.details.forEach { detail ->
                    Text(
                        text = detail.parse(
                            statuses,
                            priorities,
                            trackers
                        ),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = journal.author.name,
                style = MaterialTheme.typography.body2
            )
            Text(
                text = journal.date.formatDate(showTime = true),
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun ChildrenSection(
    children: List<IdName>,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        children.forEach { child ->
            RedmineCard(
                onClick = {
                    navController.navigate(
                        Screen.Issue.withArgs("issueId" to child.id)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = child.name,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(15.dp)
                )
            }
        }
    }
}

@Composable
fun AttachmentsSection(
    issue: Issue,
    apiKey: String,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(110.dp)
    ) {
        itemsIndexed(issue.attachments) { index, attachment ->
            var itemModifier = Modifier
                .fillMaxHeight()
                .width(300.dp)

            if (index == 0) {
                itemModifier = itemModifier.then(Modifier.padding(start = 20.dp))
            }

            if (index + 1 == issue.attachments.size) {
                itemModifier = itemModifier.then(Modifier.padding(end = 20.dp))
            }

            AttachmentItem(
                attachment = attachment,
                modifier = itemModifier
            ) {
                uriHandler.openUri(attachment.getDownloadLink(apiKey))
            }
        }
    }
}

@Composable
fun AttachmentItem(
    attachment: Attachment,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    RedmineCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            AttachmentTypeBox(
                extension = attachment.extension,
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(1.0f)
            ) {
                Text(
                    text = attachment.fileName,
                    style = MaterialTheme.typography.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (attachment.description.isNotBlank()) {
                    Text(
                        text = attachment.description,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = attachment.createdOn.formatDate(showTime = true),
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = attachment.fileSizeWithUnit,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun AttachmentTypeBox(
    extension: String,
    size: Dp = 70.dp,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(percent = 50))
            .background(MaterialTheme.colors.primary)
    ) {
        Text(
            text = extension,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun DetailsSection(
    issue: Issue,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        DetailsRow(
            name = stringResource(id = R.string.author),
            value = issue.author.name
        )
        DetailsRow(
            name = stringResource(id = R.string.assigned_to),
            value = issue.assignedTo?.name ?: "нет"
        )
        DetailsRow(
            name = stringResource(id = R.string.status),
            value = issue.status.name
        )
        DetailsRow(
            name = stringResource(id = R.string.priority),
            value = issue.priority.name
        )
        DetailsRow(
            name = stringResource(id = R.string.estimated_hours),
            value = issue.estimatedHours.formatHours()
        )
        DetailsRow(
            name = stringResource(id = R.string.spent_hours),
            value = issue.spentHours.formatHours()
        )
        DetailsRow(
            name = stringResource(id = R.string.start_date),
            value = issue.createdOn.formatDate()
        )
        DetailsRow(
            name = stringResource(id = R.string.update_date),
            value = issue.updatedOn.formatDate(showTime = true)
        )
        DetailsRow(
            name = stringResource(id = R.string.deadline),
            value = issue.deadline?.formatDate() ?: "нет"
        )
    }
}

@Composable
fun DetailsRow(name: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$name:",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        )
    }
}

@Composable
fun HeaderSection(
    issue: Issue,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
    ) {
        Text(
            text = issue.subject,
            style = MaterialTheme.typography.h5
        )
        if (issue.description?.isNotBlank() == true) {
            Text(
                text = issue.description!!,
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}