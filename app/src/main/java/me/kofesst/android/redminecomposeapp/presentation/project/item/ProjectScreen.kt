package me.kofesst.android.redminecomposeapp.presentation.project.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.presentation.LocalAppState
import me.kofesst.android.redminecomposeapp.presentation.Screen
import me.kofesst.android.redminecomposeapp.presentation.issue.SortFilterEvent
import me.kofesst.android.redminecomposeapp.presentation.issue.list.IssuesColumn
import me.kofesst.android.redminecomposeapp.presentation.issue.list.SortFilterPanel
import me.kofesst.android.redminecomposeapp.presentation.issue.list.SortFilterPanelTail
import me.kofesst.android.redminecomposeapp.presentation.util.LoadingHandler
import me.kofesst.android.redminecomposeapp.presentation.util.LoadingResult
import me.kofesst.android.redminecomposeapp.ui.component.RedmineSwipeRefresh

@Composable
fun ProjectScreen(
    projectId: Int,
    viewModel: ProjectViewModel = hiltViewModel(),
) {
    val appState = LocalAppState.current
    val navController = appState.navController
    
    LaunchedEffect(key1 = true) {
        viewModel.refreshData(projectId)
    }

    val loadingState by viewModel.loadingState
    LoadingHandler(loadingState, appState.scaffoldState.snackbarHostState)

    val isLoading = loadingState.state == LoadingResult.State.RUNNING

    val issues by viewModel.issues.collectAsState()

    val sortState by viewModel.sortState
    val filterState by viewModel.filterState

    RedmineSwipeRefresh(
        refreshState = rememberSwipeRefreshState(isLoading),
        onRefresh = { viewModel.refreshData(projectId) },
        modifier = Modifier.fillMaxSize()
    ) {
        var sortFilterPanelVisible by remember { mutableStateOf(false) }

        val trackers by viewModel.trackers.collectAsState()
        val statuses by viewModel.statuses.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {
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
                    onEndReached = {
                        if (loadingState.state != LoadingResult.State.RUNNING &&
                            viewModel.shouldLoadMore
                        ) {
                            viewModel.loadNextPage(projectId)
                        }
                    },
                    navController = navController
                )
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate(
                        Screen.CreateEditIssue.withArgs(
                            "projectId" to projectId
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_24),
                    contentDescription = null
                )
            }
        }
    }
}