package me.kofesst.android.redminecomposeapp.presentation.auth

data class AuthFormState(
    val host: String = "",
    val hostError: String? = null,
    val apiKey: String = "",
    val apiKeyError: String? = null,
)
