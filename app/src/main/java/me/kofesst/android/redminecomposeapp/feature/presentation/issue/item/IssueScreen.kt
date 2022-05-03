package me.kofesst.android.redminecomposeapp.feature.presentation.issue.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult
import me.kofesst.android.redminecomposeapp.feature.presentation.DefaultCard

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

    AnimatedVisibility(
        visible = issue != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isLoading),
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
                    DetailsSection(issue)
                    DescriptionSection(issue)
                    if (issue.attachments.isNotEmpty()) {
                        AttachmentsSection(issue)
                    }
                }
            }
        }
    }
}

@Composable
fun AttachmentsSection(issue: Issue) {
    Section {
        Text(
            text = "Вложения",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        issue.attachments.forEach { attachment ->
            Text(
                text = "${attachment.filename} - ${attachment.content_url}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DescriptionSection(issue: Issue) {
    Section {
        Text(
            text = "Описание",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = issue.description,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun DetailsSection(issue: Issue) {
    Section {
        Text(
            text = issue.subject,
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Автор: ${issue.author.name}",
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "Исполнитель: ${issue.assigned_to?.name ?: "-"}",
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "Статус: ${issue.status.name}",
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "Приоритет: ${issue.priority.name}",
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "Затраченное время: ${issue.estimated_hours}",
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "Дата начала: ${issue.start_date}",
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "Дата обновления: ${issue.updated_on}",
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "Дедлайн: ${issue.priority.name}",
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun Section(content: @Composable ColumnScope.() -> Unit) {
    DefaultCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            content()
        }
    }
}