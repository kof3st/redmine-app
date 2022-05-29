package me.kofesst.android.redminecomposeapp.presentation.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.kofesst.android.redminecomposeapp.domain.model.Account
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.domain.util.UserHolder
import me.kofesst.android.redminecomposeapp.presentation.ViewModelBase
import me.kofesst.android.redminecomposeapp.presentation.util.ValidationEvent
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCases: UseCases,
    private val userHolder: UserHolder,
) : ViewModelBase() {
    private val _accounts = MutableStateFlow<List<Account>>(listOf())
    val accounts get() = _accounts.asStateFlow()

    var formState by mutableStateOf(AuthFormState())

    private val validationChannel = Channel<ValidationEvent>()
    val validationEvents = validationChannel.receiveAsFlow()

    private val _sessionCheckState = mutableStateOf(false)
    val sessionCheckState: State<Boolean> get() = _sessionCheckState

    suspend fun checkForSession() {
        startLoading {
            val session = useCases.restoreSession()
            if (session != null) {
                formState = AuthFormState(
                    host = session.first,
                    apiKey = session.second
                )
                onDataSubmit()
            } else {
                _sessionCheckState.value = true
                loadAccounts()
            }
        }
    }

    private suspend fun loadAccounts() {
        _accounts.value = useCases.getAccounts()
    }

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
            startLoading(
                onSuccessCallback = {
                    viewModelScope.launch {
                        validationChannel.send(ValidationEvent.Success)
                    }
                }
            ) {
                userHolder.currentUser.value = useCases.getCurrentUser(
                    host = formState.host,
                    apiKey = formState.apiKey
                )
                useCases.saveSession(
                    host = formState.host,
                    apiKey = formState.apiKey
                )
            }
        }
    }
}