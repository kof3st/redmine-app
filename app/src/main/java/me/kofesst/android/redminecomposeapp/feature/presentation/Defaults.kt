package me.kofesst.android.redminecomposeapp.feature.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ClickableCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 5.dp,
    cornerRadius: Dp = 5.dp,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    DefaultCard(
        modifier = modifier.clickable { onClick() },
        elevation = elevation,
        cornerRadius = cornerRadius,
        content = content
    )
}

@Composable
fun DefaultCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 5.dp,
    cornerRadius: Dp = 5.dp,
    content: @Composable () -> Unit
) {
    Card(
        elevation = elevation,
        shape = RoundedCornerShape(cornerRadius),
        modifier = modifier,
        content = content
    )
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