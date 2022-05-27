package me.kofesst.android.redminecomposeapp.presentation.util

sealed class ValidationEvent {
    object Success : ValidationEvent()
}
