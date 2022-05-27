package me.kofesst.android.redminecomposeapp.presentation.util

data class LoadingResult(
    val state: State = State.IDLE,
    val errorMessage: String? = null
) {
    enum class State {
        IDLE, RUNNING, SUCCESS, FAILED
    }
}