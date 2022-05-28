package me.kofesst.android.redminecomposeapp.presentation.util

import android.widget.Toast
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun LoadingHandler(
    loadingState: LoadingResult,
    snackbarHostState: SnackbarHostState,
) {
    LaunchedEffect(loadingState) {
        if (loadingState.state == LoadingResult.State.FAILED) {
            snackbarHostState.showSnackbar(
                message = loadingState.errorMessage ?: "Неизвестная ошибка"
            )
        }
    }
}