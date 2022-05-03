package me.kofesst.android.redminecomposeapp.feature.presentation

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.redminecomposeapp.feature.domain.util.LoadingResult

@Composable
fun ApiContent(
    loadingState: LoadingResult,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        if (loadingState.state == LoadingResult.State.FAILED) {
            Toast.makeText(
                context,
                loadingState.errorMessage ?: "Unexpected error",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = loadingState.state == LoadingResult.State.RUNNING
    )
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            content()
        }
    }
}

@Composable
fun OutlinedValidatedTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    errorMessage: String? = null,
    onValueChange: (String) -> Unit = {},
    placeholderText: String = "",
    leadingIcon: Painter? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            isError = errorMessage != null,
            placeholder = { Text(text = placeholderText) },
            leadingIcon = if (leadingIcon != null) {
                { Icon(painter = leadingIcon, contentDescription = null) }
            } else {
                null
            },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        TextFieldError(
            modifier = Modifier.fillMaxWidth(),
            message = errorMessage
        )
    }
}

@Composable
fun TextFieldError(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    AnimatedVisibility(visible = message != null) {
        Text(
            text = message ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Start,
            modifier = modifier
        )
    }
}