package me.kofesst.android.redminecomposeapp.domain.util

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: String? = null
)
