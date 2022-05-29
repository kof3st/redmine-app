package me.kofesst.android.redminecomposeapp.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.domain.model.Account
import me.kofesst.android.redminecomposeapp.presentation.LocalAppState
import me.kofesst.android.redminecomposeapp.presentation.Screen
import me.kofesst.android.redminecomposeapp.presentation.util.LoadingHandler
import me.kofesst.android.redminecomposeapp.ui.component.*

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val appState = LocalAppState.current
    val navController = appState.navController

    LaunchedEffect(Unit) {
        viewModel.checkForSession()
    }

    FormResultHandler(viewModel.validationEvents) {
        navController.navigate(Screen.Issues.route) {
            popUpTo(Screen.Auth.route) {
                inclusive = true
            }
        }
    }

    val loadingState by viewModel.loadingState
    LoadingHandler(loadingState, appState.scaffoldState.snackbarHostState)

    if (loadingState.isLoading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }

    val accounts by viewModel.accounts.collectAsState()
    val formState = viewModel.formState

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        RedmineCard(
            cornerRadius = 20.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Авторизация",
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.height(10.dp))
                AccountsDropdown(accounts) {
                    viewModel.onFormEvent(
                        AuthFormEvent.HostChanged(
                            it.host
                        )
                    )
                    viewModel.onFormEvent(
                        AuthFormEvent.ApiKeyChanged(
                            it.apiKey
                        )
                    )
                }
                RedmineTextField(
                    value = formState.host,
                    onValueChange = { viewModel.onFormEvent(AuthFormEvent.HostChanged(it)) },
                    errorMessage = formState.hostError,
                    label = "Хост",
                    leadingIcon = painterResource(R.drawable.ic_host_24),
                    modifier = Modifier.fillMaxWidth()
                )
                RedmineTextField(
                    value = formState.apiKey,
                    onValueChange = { viewModel.onFormEvent(AuthFormEvent.ApiKeyChanged(it)) },
                    errorMessage = formState.apiKeyError,
                    label = "API-ключ",
                    leadingIcon = painterResource(R.drawable.ic_api_key_24),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                SubmitButton(modifier = Modifier.fillMaxWidth()) {
                    viewModel.onFormEvent(AuthFormEvent.Submit)
                }
            }
        }
    }
}

@Composable
fun AccountsDropdown(
    accounts: List<Account>,
    onAccountSelected: (Account) -> Unit,
) {
    var selected by remember {
        mutableStateOf<Account?>(null)
    }

    AnimatedVisibility(visible = accounts.isNotEmpty()) {
        RedmineDropdown(
            items = accounts.map { account ->
                DropdownItem(
                    text = account.name,
                    onSelected = {
                        selected = account
                        onAccountSelected(account)
                    }
                )
            },
            value = selected?.name ?: "",
            placeholder = "Аккаунт",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SubmitButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        text = {
            Text(
                text = "Отправить запрос",
                style = MaterialTheme.typography.body1
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = null
            )
        },
        modifier = modifier
    )
}