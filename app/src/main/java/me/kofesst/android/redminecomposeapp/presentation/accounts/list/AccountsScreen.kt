package me.kofesst.android.redminecomposeapp.presentation.accounts.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.domain.model.Account
import me.kofesst.android.redminecomposeapp.presentation.LocalAppState
import me.kofesst.android.redminecomposeapp.presentation.Screen
import me.kofesst.android.redminecomposeapp.presentation.util.LoadingResult
import me.kofesst.android.redminecomposeapp.ui.component.RedmineCard
import me.kofesst.android.redminecomposeapp.ui.component.RedmineSwipeRefresh

@Composable
fun AccountsScreen(viewModel: AccountsViewModel) {
    val appState = LocalAppState.current
    val navController = appState.navController

    LaunchedEffect(key1 = true) {
        viewModel.refreshData()
    }

    val loadingState by viewModel.loadingState
    val isLoading = loadingState.state == LoadingResult.State.RUNNING

    val accounts by viewModel.accounts.collectAsState()

    RedmineSwipeRefresh(
        refreshState = rememberSwipeRefreshState(isLoading),
        onRefresh = { viewModel.refreshData() },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = rememberLazyListState(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(accounts) { _, account ->
                        AccountItem(account) {
                            navController.navigate(
                                Screen.CreateEditAccount.withArgs(
                                    "accountId" to account.id
                                )
                            )
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate(
                        Screen.CreateEditAccount.withArgs()
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

@Composable
fun AccountItem(
    account: Account,
    onItemClick: () -> Unit,
) {
    Box(modifier = Modifier.padding(10.dp)) {
        RedmineCard(
            onClick = onItemClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = account.host,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = account.apiKey,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}