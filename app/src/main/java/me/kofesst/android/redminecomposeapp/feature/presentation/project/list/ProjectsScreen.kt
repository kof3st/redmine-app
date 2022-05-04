package me.kofesst.android.redminecomposeapp.feature.presentation.project.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.feature.data.model.project.Project
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult
import me.kofesst.android.redminecomposeapp.feature.presentation.Screen

@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel,
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        viewModel.refreshData()
    }

    val loadingState by viewModel.loadingState
    val isLoading = loadingState.state == LoadingResult.State.RUNNING

    val projects by viewModel.projects.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isLoading),
            onRefresh = { viewModel.refreshData() },
            modifier = Modifier.fillMaxSize()
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
}

@Composable
fun ProjectItem(
    project: Project,
    onItemClick: () -> Unit
) {
    Box(modifier = Modifier.padding(10.dp)) {
        Card(
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick() }
        ) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}