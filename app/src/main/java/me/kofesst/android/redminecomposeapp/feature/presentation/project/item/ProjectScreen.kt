package me.kofesst.android.redminecomposeapp.feature.presentation.project.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.SortFilterEvent
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.list.IssuesColumn
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.list.SortFilterPanel
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.list.SortFilterPanelTail

@Composable
fun ProjectScreen(
    projectId: Int,
    viewModel: ProjectViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        viewModel.refreshData(projectId)
    }

    val loadingState by viewModel.loadingState
    val isLoading = loadingState.state == LoadingResult.State.RUNNING

    val issues by viewModel.issues.collectAsState()

    val sortState by viewModel.sortState
    val filterState by viewModel.filterState

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = { viewModel.refreshData(projectId) },
        modifier = Modifier.fillMaxSize()
    ) {
        var sortFilterPanelVisible by remember { mutableStateOf(false) }

        val trackers by viewModel.trackers.collectAsState()
        val statuses by viewModel.statuses.collectAsState()

        Column(modifier = Modifier.fillMaxSize()) {
            SortFilterPanel(
                visible = sortFilterPanelVisible,
                sortState = sortState,
                onSortStateChanged = {
                    viewModel.onSortFilterEvent(
                        SortFilterEvent.SortStateChanged(
                            sortState = it
                        )
                    )
                },
                filterState = filterState,
                onFilterStateChanged = {
                    viewModel.onSortFilterEvent(
                        SortFilterEvent.FilterStateChanged(
                            filterState = it
                        )
                    )
                },
                trackers = trackers,
                statuses = statuses
            )
            SortFilterPanelTail {
                sortFilterPanelVisible = !sortFilterPanelVisible
            }
            IssuesColumn(
                issues = issues,
                navController = navController
            )
        }
    }
}