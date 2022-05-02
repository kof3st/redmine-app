package me.kofesst.android.redminecomposeapp.feature.presentation.auth

sealed class AuthFormEvent {
    data class HostChanged(val host: String) : AuthFormEvent()
    data class ApiKeyChanged(val apiKey: String) : AuthFormEvent()
    object Submit : AuthFormEvent()
}