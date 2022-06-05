package me.kofesst.android.redminecomposeapp.presentation.issue.list

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.presentation.LocalAppState
import me.kofesst.android.redminecomposeapp.presentation.Screen
import me.kofesst.android.redminecomposeapp.presentation.issue.SortFilterEvent
import me.kofesst.android.redminecomposeapp.presentation.util.*
import me.kofesst.android.redminecomposeapp.ui.component.*

@Composable
fun IssuesScreen(viewModel: IssuesViewModel) {
    val appState = LocalAppState.current
    val navController = appState.navController

    LaunchedEffect(key1 = true) {
        viewModel.refreshData()
    }

    val loadingState by viewModel.loadingState
    LoadingHandler(loadingState, appState.scaffoldState.snackbarHostState)

    val isLoading = loadingState.state == LoadingResult.State.RUNNING

    val currentTab by viewModel.currentTab
    val issues by viewModel.issues.collectAsState()

    val sortState by viewModel.sortState
    val filterState by viewModel.filterState

    val tabs = listOf(
        IssuesTab.Assigned,
        IssuesTab.Owned
    )

    RedmineSwipeRefresh(
        refreshState = rememberSwipeRefreshState(isLoading),
        onRefresh = { viewModel.refreshData() },
        modifier = Modifier.fillMaxSize()
    ) {
        var sortFilterPanelVisible by remember { mutableStateOf(false) }

        val trackers by viewModel.trackers.collectAsState()
        val statuses by viewModel.statuses.collectAsState()

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
            SortFilterPanel(
                visible = sortFilterPanelVisible,
                sortState = sortState,
                onSortStateChanged = {
                    viewModel.onSortFilterEvent(
                        SortFilterEvent.SortStateChanged(
                            sortState = it
                        )
                    )
                    viewModel.refreshData()
                },
                filterState = filterState,
                onFilterStateChanged = {
                    viewModel.onSortFilterEvent(
                        SortFilterEvent.FilterStateChanged(
                            filterState = it
                        )
                    )
                    viewModel.refreshData()
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

@Composable
fun SortFilterPanelTail(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_filter_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Фильтр",
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun SortFilterPanel(
    visible: Boolean,
    sortState: IssueSortState,
    onSortStateChanged: (IssueSortState) -> Unit,
    filterState: IssueFilterState?,
    onFilterStateChanged: (IssueFilterState?) -> Unit,
    trackers: List<IdName>,
    statuses: List<IdName>,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                SortStateDropdowns(
                    sortState = sortState,
                    onStateChanged = onSortStateChanged
                )
                FilterStateDropdown(
                    filterState = filterState,
                    onStateChanged = onFilterStateChanged,
                    trackers = trackers,
                    statuses = statuses
                )
            }
        }
    }
}

@Composable
fun FilterStateDropdown(
    filterState: IssueFilterState?,
    onStateChanged: (IssueFilterState?) -> Unit,
    trackers: List<IdName>,
    statuses: List<IdName>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Фильтрация",
            style = MaterialTheme.typography.body1
        )

        val filterStates = listOf(
            IssueFilterState.ByTracker(),
            IssueFilterState.ByStatus(),
        )
        RedmineDropdown(
            items = filterStates.map { state ->
                DropdownItem(
                    text = stringResource(state.nameRes),
                    onSelected = {
                        onStateChanged(state)
                    }
                )
            } + DropdownItem(
                text = "Нет",
                onSelected = {
                    onStateChanged(null)
                }
            ),
            value = if (filterState != null) {
                stringResource(filterState.nameRes)
            } else {
                "Нет"
            },
            modifier = Modifier.fillMaxWidth()
        )

        val filterValues = filterState?.let {
            when (it) {
                is IssueFilterState.ByTracker -> {
                    trackers
                }
                is IssueFilterState.ByStatus -> {
                    statuses
                }
            }
        } ?: listOf()

        if (filterValues.isNotEmpty()) {
            RedmineDropdown(
                items = filterValues.map { value ->
                    DropdownItem(
                        text = value.toString(),
                        onSelected = {
                            when (filterState) {
                                is IssueFilterState.ByTracker -> {
                                    onStateChanged(
                                        filterState.copy(
                                            tracker = value
                                        )
                                    )
                                }
                                is IssueFilterState.ByStatus -> {
                                    onStateChanged(
                                        filterState.copy(
                                            status = value
                                        )
                                    )
                                }
                                else -> {}
                            }
                        }
                    )
                },
                value = filterState?.item?.toString() ?: "",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SortStateDropdowns(
    sortState: IssueSortState,
    onStateChanged: (IssueSortState) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Сортировка",
            style = MaterialTheme.typography.body1
        )

        val sortStates = listOf(
            IssueSortState.ById(sortState.orderType),
            IssueSortState.ByPriority(sortState.orderType)
        )
        RedmineDropdown(
            items = sortStates.map { state ->
                DropdownItem(
                    text = stringResource(state.nameRes),
                    onSelected = {
                        onStateChanged(state)
                    }
                )
            },
            value = stringResource(sortState.nameRes),
            modifier = Modifier.fillMaxWidth()
        )

        val orderTypes = listOf(
            OrderType.Ascending,
            OrderType.Descending
        )
        RedmineDropdown(
            items = orderTypes.map { type ->
                DropdownItem(
                    text = stringResource(type.nameRes),
                    onSelected = {
                        onStateChanged(sortState.copy(type))
                    }
                )
            },
            value = stringResource(sortState.orderType.nameRes),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun IssuesColumn(
    issues: List<Issue>,
    onEndReached: () -> Unit = {},
    navController: NavController,
) {
    SourceHandler(
        source = issues,
        emptySourceTextRes = R.string.empty_issues
    ) {
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(issues) { index, issue ->
                if (index == issues.lastIndex) {
                    onEndReached()
                }

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
}

@Composable
fun IssueItem(
    issue: Issue,
    onItemClick: () -> Unit,
) {
    Box(modifier = Modifier.padding(10.dp)) {
        RedmineCard(
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
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Автор: ${issue.author.name}",
                    style = MaterialTheme.typography.body1
                )
                issue.assignedTo?.run {
                    Text(
                        text = "Исполнитель: ${this.name}",
                        style = MaterialTheme.typography.body1
                    )
                }
                Text(
                    text = "Статус: ${issue.status.name}",
                    style = MaterialTheme.typography.body1
                )
                if (issue.priority.id > 4) {
                    IssuePriority(
                        priority = issue.priority,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(CenterHorizontally)
                            .padding(top = 15.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun IssuePriority(
    priority: IdName,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "${priority.name} приоритет".uppercase(),
        style = MaterialTheme.typography.body1,
        color = Color.Red,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}