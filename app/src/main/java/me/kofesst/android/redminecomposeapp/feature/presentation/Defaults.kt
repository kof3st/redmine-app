package me.kofesst.android.redminecomposeapp.feature.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun OutlinedValidatedTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    errorMessage: String? = null,
    onValueChange: (String) -> Unit = {},
    placeholderText: String = ""
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            isError = errorMessage != null,
            placeholder = { Text(text = placeholderText) },
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