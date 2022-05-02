package me.kofesst.android.redminecomposeapp.feature.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.domain.util.ValidationEvent
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {
    var formState by mutableStateOf(AuthFormState())

    private val validationChannel = Channel<ValidationEvent>()
    val validationEvents = validationChannel.receiveAsFlow()

    fun onFormEvent(event: AuthFormEvent) {
        when (event) {
            is AuthFormEvent.HostChanged -> {
                formState = formState.copy(host = event.host)
            }
            is AuthFormEvent.ApiKeyChanged -> {
                formState = formState.copy(apiKey = event.apiKey)
            }
            is AuthFormEvent.Submit -> {
                onDataSubmit()
            }
        }
    }

    private fun onDataSubmit() {
        val hostResult = useCases.validateForEmptyField(formState.host)
        val apiKeyResult = useCases.validateForEmptyField(formState.apiKey)

        formState = formState.copy(
            hostError = hostResult.errorMessage,
            apiKeyError = apiKeyResult.errorMessage
        )

        val hasError = listOf(
            hostResult,
            apiKeyResult
        ).any { it.errorMessage != null }

        if (!hasError) {
            viewModelScope.launch {
                validationChannel.send(ValidationEvent.Success)
            }
            return
        }
    }
}