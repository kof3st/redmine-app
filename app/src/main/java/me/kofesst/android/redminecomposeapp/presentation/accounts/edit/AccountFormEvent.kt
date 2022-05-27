package me.kofesst.android.redminecomposeapp.presentation.accounts.edit

sealed class AccountFormEvent {
    data class NameChanged(val name: String) : AccountFormEvent()
    data class HostChanged(val host: String) : AccountFormEvent()
    data class ApiKeyChanged(val apiKey: String) : AccountFormEvent()
    object Submit : AccountFormEvent()
    object Delete : AccountFormEvent()
}