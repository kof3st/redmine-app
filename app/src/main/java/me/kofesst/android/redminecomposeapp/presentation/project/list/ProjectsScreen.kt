package me.kofesst.android.redminecomposeapp.presentation.project.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.domain.model.Project
import me.kofesst.android.redminecomposeapp.domain.util.formatDate
import me.kofesst.android.redminecomposeapp.presentation.LocalAppState
import me.kofesst.android.redminecomposeapp.presentation.Screen
import me.kofesst.android.redminecomposeapp.presentation.util.LoadingHandler
import me.kofesst.android.redminecomposeapp.presentation.util.LoadingResult
import me.kofesst.android.redminecomposeapp.ui.component.RedmineCard
import me.kofesst.android.redminecomposeapp.ui.component.RedmineSwipeRefresh
import me.kofesst.android.redminecomposeapp.ui.component.SourceHandler

@Composable
fun ProjectsScreen(viewModel: ProjectsViewModel) {
    val appState = LocalAppState.current
    val navController = appState.navController

    LaunchedEffect(key1 = true) {
        viewModel.refreshData()
    }

    val loadingState by viewModel.loadingState
    LoadingHandler(loadingState, appState.scaffoldState.snackbarHostState)

    val isLoading = loadingState.state == LoadingResult.State.RUNNING

    val projects by viewModel.projects.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        RedmineSwipeRefresh(
            refreshState = rememberSwipeRefreshState(isLoading),
            onRefresh = { viewModel.refreshData() },
            modifier = Modifier.fillMaxSize()
        ) {
            ProjectsList(
                projects = projects,
                navController = navController
            )
        }
    }
}

@Composable
fun ProjectsList(
    projects: List<Project>,
    navController: NavController,
) {
    SourceHandler(
        source = projects,
        emptySourceTextRes = R.string.empty_projects
    ) {
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(projects) { _, project ->
                ProjectItem(project) {
                    navController.navigate(
                        Screen.Project.withArgs(
                            "projectId" to project.id
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectItem(
    project: Project,
    onItemClick: () -> Unit,
) {
    Box(modifier = Modifier.padding(10.dp)) {
        RedmineCard(
            onClick = onItemClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(
                        id = R.string.project_update_date,
                        project.updatedOn.formatDate(showTime = true)
                    ),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}