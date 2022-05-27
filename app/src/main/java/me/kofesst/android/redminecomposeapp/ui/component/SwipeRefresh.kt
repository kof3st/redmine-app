package me.kofesst.android.redminecomposeapp.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
fun RedmineSwipeRefresh(
    refreshState: SwipeRefreshState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    SwipeRefresh(
        state = refreshState,
        onRefresh = onRefresh,
        indicator = { state, trigger ->
            RedmineSwipeRefreshIndicator(
                state = state,
                trigger = trigger
            )
        },
        content = content,
        modifier = modifier
    )
}

@Composable
fun RedmineSwipeRefreshIndicator(
    state: SwipeRefreshState,
    trigger: Dp,
) {
    SwipeRefreshIndicator(
        state = state,
        refreshTriggerDistance = trigger,
        scale = true,
        contentColor = MaterialTheme.colors.primary
    )
}