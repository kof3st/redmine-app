package me.kofesst.android.redminecomposeapp.feature.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.kofesst.android.redminecomposeapp.feature.domain.util.ValidationEvent
import me.kofesst.android.redminecomposeapp.feature.presentation.OutlinedValidatedTextField
import me.kofesst.android.redminecomposeapp.feature.presentation.Screen

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    navController.navigate(Screen.Issues.route)
                }
            }
        }
    }

    val formState = viewModel.formState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        OutlinedValidatedTextField(
            value = formState.host,
            onValueChange = { viewModel.onFormEvent(AuthFormEvent.HostChanged(it)) },
            errorMessage = formState.hostError,
            placeholderText = "Хост",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedValidatedTextField(
            value = formState.apiKey,
            onValueChange = { viewModel.onFormEvent(AuthFormEvent.ApiKeyChanged(it)) },
            errorMessage = formState.apiKeyError,
            placeholderText = "API-ключ",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.onFormEvent(AuthFormEvent.Submit)
            }
        ) {
            Text(text = "Send data")
        }
    }
}