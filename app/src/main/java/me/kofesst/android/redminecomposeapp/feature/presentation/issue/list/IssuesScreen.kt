package me.kofesst.android.redminecomposeapp.feature.presentation.issue.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult
import me.kofesst.android.redminecomposeapp.feature.presentation.ClickableCard
import me.kofesst.android.redminecomposeapp.feature.presentation.Screen

@Composable
fun IssuesScreen(
    viewModel: IssuesViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        viewModel.refreshData()
    }

    val loadingState by viewModel.loadingState
    val isLoading = loadingState.state == LoadingResult.State.RUNNING

    val currentTab by viewModel.currentTab
    val issues by viewModel.tabIssues.collectAsState()

    val tabs = listOf(
        IssuesTab.Assigned,
        IssuesTab.Owned
    )

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = { viewModel.refreshData() },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = currentTab.index) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = currentTab.index == index,
                        onClick = { viewModel.onTabSelected(tab) },
                        text = {
                            Text(
                                text = stringResource(tab.titleRes),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    )
                }
            }
            IssuesColumn(
                issues = issues,
                navController = navController
            )
        }
    }
}

@Composable
fun IssuesColumn(
    issues: List<Issue>,
    navController: NavController
) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(issues) { _, issue ->
            IssueItem(
                issue = issue,
                onItemClick = {
                    navController.navigate(
                        Screen.Issue.withArgs(
                            "issueId" to issue.id
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun IssueItem(
    issue: Issue,
    onItemClick: () -> Unit
) {
    Box(modifier = Modifier.padding(10.dp)) {
        ClickableCard(
            onClick = onItemClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = issue.subject,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${issue.tracker.name} #${issue.id}",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Автор: ${issue.author.name}",
                    style = MaterialTheme.typography.body1
                )
                issue.assigned_to?.run {
                    Text(
                        text = "Исполнитель: ${this.name}",
                        style = MaterialTheme.typography.body1
                    )
                }
                Text(
                    text = "Статус: ${issue.status.name}",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}