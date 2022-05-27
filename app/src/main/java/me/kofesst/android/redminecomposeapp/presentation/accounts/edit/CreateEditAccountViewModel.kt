package me.kofesst.android.redminecomposeapp.presentation.accounts.edit

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.kofesst.android.redminecomposeapp.domain.model.Account
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.presentation.ViewModelBase
import me.kofesst.android.redminecomposeapp.presentation.util.ValidationEvent
import javax.inject.Inject

@HiltViewModel
class CreateEditAccountViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModelBase() {
    var formState by mutableStateOf(AccountFormState())

    private val _editing = mutableStateOf<Account?>(null)
    val editing: State<Account?> get() = _editing

    fun loadDetails(editingId: Int) {
        startLoading {
            _editing.value = useCases.getAccount(editingId)

            _editing.value?.run {
                formState = AccountFormState(
                    name = this.name,
                    host = this.host,
                    apiKey = this.apiKey
                )
            }
        }
    }

    private val validationChannel = Channel<ValidationEvent>()
    val validationEvents = validationChannel.receiveAsFlow()

    fun onFormEvent(event: AccountFormEvent) {
        when (event) {
            is AccountFormEvent.NameChanged -> {
                formState = formState.copy(name = event.name)
            }
            is AccountFormEvent.HostChanged -> {
                formState = formState.copy(host = event.host)
            }
            is AccountFormEvent.ApiKeyChanged -> {
                formState = formState.copy(apiKey = event.apiKey)
            }
            is AccountFormEvent.Submit -> {
                onDataSubmit()
            }
            is AccountFormEvent.Delete -> {
                onDelete()
            }
        }
    }

    private fun onDataSubmit() {
        val nameResult = useCases.validateForEmptyField(formState.name)
        val hostResult = useCases.validateForEmptyField(formState.host)
        val apiKeyResult = useCases.validateForEmptyField(formState.apiKey)

        formState = formState.copy(
            nameError = nameResult.errorMessage,
            hostError = hostResult.errorMessage,
            apiKeyError = apiKeyResult.errorMessage
        )

        val hasError = listOf(
            nameResult,
            hostResult,
            apiKeyResult
        ).any { it.errorMessage != null }

        listOf<String>().any { it.isEmpty() }

        if (!hasError) {
            startLoading(
                onSuccessCallback = {
                    viewModelScope.launch {
                        validationChannel.send(ValidationEvent.Success)
                    }
                }
            ) {
                _editing.value?.run {
                    useCases.updateAccount(
                        this.copy(
                            name = formState.name,
                            host = formState.host,
                            apiKey = formState.apiKey
                        )
                    )
                } ?: kotlin.run {
                    useCases.addAccount(
                        Account(
                            name = formState.name,
                            host = formState.host,
                            apiKey = formState.apiKey
                        )
                    )
                }
            }
        }
    }

    private fun onDelete() {
        startLoading(
            onSuccessCallback = {
                viewModelScope.launch {
                    validationChannel.send(ValidationEvent.Success)
                }
            }
        ) {
            _editing.value?.run {
                useCases.deleteAccount(this)
            }
        }
    }
}