package me.kofesst.android.redminecomposeapp.feature.presentation.util

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult
import me.kofesst.android.redminecomposeapp.feature.presentation.ViewModelBase

@Composable
fun LoadingHandler(
    viewModel: ViewModelBase
) {
    val context = LocalContext.current
    val loadingState by viewModel.loadingState
    LaunchedEffect(loadingState) {
        if (loadingState.state == LoadingResult.State.FAILED) {
            Toast.makeText(
                context,
                loadingState.errorMessage ?: "Unexpected error",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}