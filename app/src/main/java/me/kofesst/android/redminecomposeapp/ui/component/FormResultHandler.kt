package me.kofesst.android.redminecomposeapp.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import me.kofesst.android.redminecomposeapp.presentation.util.ValidationEvent

@Composable
fun FormResultHandler(
    results: Flow<ValidationEvent>,
    onSuccess: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        results.collect {
            when (it) {
                is ValidationEvent.Success -> onSuccess()
            }
        }
    }
}