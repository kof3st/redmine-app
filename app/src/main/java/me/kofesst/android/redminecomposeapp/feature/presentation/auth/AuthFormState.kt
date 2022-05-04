package me.kofesst.android.redminecomposeapp.feature.presentation.auth

data class AuthFormState(
    val host: String = "redmine-mobile.sitesoft.ru",
    val hostError: String? = null,
    val apiKey: String = "7f312312fd8dd9f249bd549d083d3c9e02a4b7cc",
    val apiKeyError: String? = null
)
