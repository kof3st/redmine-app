package me.kofesst.android.redminecomposeapp.feature.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
fun DefaultSwipeRefresh(
    refreshState: SwipeRefreshState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    content: @Composable () -> Unit
) {
    SwipeRefresh(
        state = refreshState,
        onRefresh = onRefresh,
        indicator = { state, trigger ->
            DefaultSwipeRefreshIndicator(
                state = state,
                trigger = trigger
            )
        },
        content = content,
        modifier = modifier
    )
}

@Composable
fun DefaultSwipeRefreshIndicator(
    state: SwipeRefreshState,
    trigger: Dp
) {
    SwipeRefreshIndicator(
        state = state,
        refreshTriggerDistance = trigger,
        scale = true,
        contentColor = MaterialTheme.colors.primary
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Dropdown(
    items: List<DropdownItem>,
    value: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            value = value,
            onValueChange = { },
            maxLines = 1,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    item.onSelected()
                    isExpanded = false
                }) {
                    Text(
                        text = item.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

data class DropdownItem(
    val text: String,
    val onSelected: () -> Unit
)

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