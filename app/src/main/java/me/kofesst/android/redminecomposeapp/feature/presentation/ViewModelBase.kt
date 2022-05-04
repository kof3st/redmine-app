package me.kofesst.android.redminecomposeapp.feature.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult

abstract class ViewModelBase : ViewModel() {
    private val _loadingState = mutableStateOf(LoadingResult())
    val loadingState: State<LoadingResult> get() = _loadingState

    private var _job: Job? = null

    fun startLoading(
        onSuccessCallback: () -> Unit = {},
        block: suspend () -> Unit
    ) {
        _job?.cancel()
        _job = viewModelScope.launch {
            _loadingState.value = loadingState.value.copy(state = LoadingResult.State.RUNNING)
            try {
                block()
                _loadingState.value = loadingState.value.copy(state = LoadingResult.State.SUCCESS)
                onSuccessCallback()
            } catch (e: Exception) {
                _loadingState.value = loadingState.value.copy(
                    state = LoadingResult.State.FAILED,
                    errorMessage = e.message ?: "Unexpected error"
                )
                Log.d("AAA", e.stackTraceToString())
            }
        }
    }
}