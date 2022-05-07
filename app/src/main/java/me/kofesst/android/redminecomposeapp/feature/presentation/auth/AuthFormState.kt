package me.kofesst.android.redminecomposeapp.feature.presentation.auth

data class AuthFormState(
    val host: String = "",
    val hostError: String? = null,
    val apiKey: String = "",
    val apiKeyError: String? = null,
)
