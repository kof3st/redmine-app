package me.kofesst.android.redminecomposeapp.feature.domain.util

sealed class ValidationEvent {
    object Success : ValidationEvent()
}
