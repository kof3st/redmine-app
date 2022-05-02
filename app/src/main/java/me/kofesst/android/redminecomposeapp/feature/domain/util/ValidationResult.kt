package me.kofesst.android.redminecomposeapp.feature.domain.util

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: String? = null
)
