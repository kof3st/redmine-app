package me.kofesst.android.redminecomposeapp.feature.presentation.auth

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
import androidx.navigation.NavController
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.feature.data.model.account.Account
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult
import me.kofesst.android.redminecomposeapp.feature.domain.util.ValidationEvent
import me.kofesst.android.redminecomposeapp.feature.presentation.*
import me.kofesst.android.redminecomposeapp.feature.presentation.util.LoadingHandler

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
) {
    LaunchedEffect(key1 = true) {
        viewModel.checkForSession()
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    navController.navigate(Screen.Issues.route)
                }
            }
        }
    }

    val loadingState by viewModel.loadingState
    LoadingHandler(viewModel)

    val isLoading = loadingState.state == LoadingResult.State.RUNNING
    if (isLoading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }

    val accounts by viewModel.accounts.collectAsState()
    var selectedAccount by remember {
        mutableStateOf<Account?>(null)
    }

    val formState = viewModel.formState

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        DefaultCard(
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
                AnimatedVisibility(visible = accounts.isNotEmpty()) {
                    Dropdown(
                        items = accounts.map { account ->
                            DropdownItem(
                                text = account.name,
                                onSelected = {
                                    selectedAccount = account
                                    viewModel.onFormEvent(
                                        AuthFormEvent.HostChanged(
                                            account.host
                                        )
                                    )
                                    viewModel.onFormEvent(
                                        AuthFormEvent.ApiKeyChanged(
                                            account.apiKey
                                        )
                                    )
                                }
                            )
                        },
                        value = selectedAccount?.name ?: "",
                        placeholder = "Аккаунт",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedValidatedTextField(
                    value = formState.host,
                    onValueChange = { viewModel.onFormEvent(AuthFormEvent.HostChanged(it)) },
                    errorMessage = formState.hostError,
                    label = "Хост",
                    leadingIcon = painterResource(R.drawable.ic_host_24),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedValidatedTextField(
                    value = formState.apiKey,
                    onValueChange = { viewModel.onFormEvent(AuthFormEvent.ApiKeyChanged(it)) },
                    errorMessage = formState.apiKeyError,
                    label = "API-ключ",
                    leadingIcon = painterResource(R.drawable.ic_api_key_24),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.onFormEvent(AuthFormEvent.Submit)
                    },
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
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}