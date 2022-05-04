package me.kofesst.android.redminecomposeapp.feature.presentation.issue.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.ChildIssue
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.data.model.journal.Journal
import me.kofesst.android.redminecomposeapp.feature.data.model.status.Status
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult
import me.kofesst.android.redminecomposeapp.feature.domain.util.formatDate
import me.kofesst.android.redminecomposeapp.feature.domain.util.formatHours
import me.kofesst.android.redminecomposeapp.feature.domain.util.getInfoText
import me.kofesst.android.redminecomposeapp.feature.presentation.ClickableCard
import me.kofesst.android.redminecomposeapp.feature.presentation.DefaultCard
import me.kofesst.android.redminecomposeapp.feature.presentation.DefaultSwipeRefresh
import me.kofesst.android.redminecomposeapp.feature.presentation.Screen

@Composable
fun IssueScreen(
    issueId: Int,
    viewModel: IssueViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        viewModel.refreshData(issueId)
    }

    val loadingState by viewModel.loadingState
    val isLoading = loadingState.state == LoadingResult.State.RUNNING

    val issue by viewModel.issue.collectAsState()
    val statuses by viewModel.statuses.collectAsState()

    AnimatedVisibility(
        visible = issue != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        DefaultSwipeRefresh(
            refreshState = rememberSwipeRefreshState(isLoading),
            onRefresh = { viewModel.refreshData(issueId) },
            modifier = Modifier.fillMaxSize()
        ) {
            issue?.also { issue ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    HeaderSection(issue)
                    Divider(modifier = Modifier.padding(vertical = 10.dp))
                    DetailsSection(issue)
                    if (issue.attachments.isNotEmpty()) {
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                        AttachmentsSection(issue)
                    }
                    if (issue.children?.isNotEmpty() == true) {
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                        ChildrenSection(
                            children = issue.children,
                            navController = navController
                        )
                    }
                    if (issue.journals.isNotEmpty()) {
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                        JournalsSection(
                            issue = issue,
                            statuses = statuses
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JournalsSection(
    issue: Issue,
    statuses: List<Status>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        issue.journals.forEach { journal ->
            JournalItem(journal, statuses)
        }
    }
}

@Composable
fun JournalItem(
    journal: Journal,
    statuses: List<Status>
) {
    DefaultCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                text = "Изменения №${journal.id}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            if (journal.notes != null) {
                Text(
                    text = journal.notes,
                    style = MaterialTheme.typography.body1
                )
            }
            if (journal.details.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                journal.details.forEach { detail ->
                    Text(
                        text = detail.getInfoText(statuses),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = journal.user.name,
                style = MaterialTheme.typography.body2
            )
            Text(
                text = journal.created_on.formatDate(showTime = true),
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun ChildrenSection(
    children: List<ChildIssue>,
    navController: NavController
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        children.forEach { child ->
            ClickableCard(
                onClick = {
                    navController.navigate(
                        Screen.Issue.withArgs("issueId" to child.id)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = child.subject,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(15.dp)
                )
            }
        }
    }
}

@Composable
fun AttachmentsSection(issue: Issue) {
    val uriHandler = LocalUriHandler.current

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        issue.attachments.forEach { attachment ->
            val attachmentLink = buildAnnotatedString {
                pushStringAnnotation(tag = "file-url", annotation = attachment.content_url)
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                    append(attachment.filename)
                }
                pop()
            }

            ClickableText(
                text = attachmentLink,
                style = MaterialTheme.typography.body1
            ) { offset ->
                attachmentLink.getStringAnnotations(
                    tag = "file-url",
                    start = offset,
                    end = offset
                ).firstOrNull()?.run {
                    uriHandler.openUri(this.item)
                }
            }
        }
    }
}

@Composable
fun DetailsSection(issue: Issue) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        DetailsRow(name = "Автор", value = issue.author.name)
        DetailsRow(name = "Исполнитель", value = issue.assigned_to?.name ?: "нет")
        DetailsRow(name = "Статус", value = issue.status.name)
        DetailsRow(name = "Приоритет", value = issue.priority.name)
        DetailsRow(name = "Оценочное время", value = issue.estimated_hours.formatHours())
        DetailsRow(name = "Затраченное время", value = issue.spent_hours.formatHours())
        DetailsRow(name = "Дата начала", value = issue.start_date.formatDate())
        DetailsRow(name = "Дата обновления", value = issue.updated_on.formatDate(showTime = true))
        DetailsRow(name = "Дедлайн", value = issue.deadline?.formatDate() ?: "нет")
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
fun HeaderSection(issue: Issue) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = issue.subject,
            style = MaterialTheme.typography.h5
        )
        issue.description?.run {
            Text(
                text = this,
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}