package me.kofesst.android.redminecomposeapp.presentation.accounts.edit

data class AccountFormState(
    val name: String = "",
    val nameError: String? = null,
    val host: String = "",
    val hostError: String? = null,
    val apiKey: String = "",
    val apiKeyError: String? = null
)
