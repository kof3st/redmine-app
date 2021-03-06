package me.kofesst.android.redminecomposeapp.presentation.accounts.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.presentation.LocalAppState
import me.kofesst.android.redminecomposeapp.presentation.util.ValidationEvent
import me.kofesst.android.redminecomposeapp.ui.component.RedmineTextField

@Composable
fun CreateEditAccountScreen(
    accountId: Int,
    viewModel: CreateEditAccountViewModel = hiltViewModel(),
) {
    val appState = LocalAppState.current
    val navController = appState.navController

    LaunchedEffect(key1 = true) {
        viewModel.loadDetails(accountId)
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    navController.navigateUp()
                }
            }
        }
    }

    val editing by viewModel.editing
    val formState = viewModel.formState

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            RedmineTextField(
                value = formState.name,
                onValueChange = { viewModel.onFormEvent(AccountFormEvent.NameChanged(it)) },
                errorMessage = formState.nameError,
                label = stringResource(id = R.string.object_name),
                leadingIcon = painterResource(R.drawable.ic_short_text_24),
                modifier = Modifier.fillMaxWidth()
            )
            RedmineTextField(
                value = formState.host,
                onValueChange = { viewModel.onFormEvent(AccountFormEvent.HostChanged(it)) },
                errorMessage = formState.hostError,
                label = stringResource(id = R.string.host),
                leadingIcon = painterResource(R.drawable.ic_host_24),
                modifier = Modifier.fillMaxWidth()
            )
            RedmineTextField(
                value = formState.apiKey,
                onValueChange = { viewModel.onFormEvent(AccountFormEvent.ApiKeyChanged(it)) },
                errorMessage = formState.apiKeyError,
                label = stringResource(id = R.string.api_key),
                leadingIcon = painterResource(R.drawable.ic_api_key_24),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.onFormEvent(AccountFormEvent.Submit)
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.save),
                        style = MaterialTheme.typography.body1
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_accounts_24),
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            editing?.run {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.onFormEvent(AccountFormEvent.Delete)
                    },
                    text = {
                        Text(
                            text = stringResource(id = R.string.delete),
                            style = MaterialTheme.typography.body1
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete_24),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}