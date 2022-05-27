package me.kofesst.android.redminecomposeapp.presentation.auth

sealed class AuthFormEvent {
    data class HostChanged(val host: String) : AuthFormEvent()
    data class ApiKeyChanged(val apiKey: String) : AuthFormEvent()
    object Submit : AuthFormEvent()
}