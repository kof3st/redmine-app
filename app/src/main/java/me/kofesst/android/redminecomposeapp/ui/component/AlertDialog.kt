package me.kofesst.android.redminecomposeapp.ui.component

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun RedmineAlertDialog(
    title: String,
    text: String,
    onDismissRequest: () -> Unit = {},
    onConfirmRequest: () -> Unit = {},
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
) {
    val color = MaterialTheme.colors.surface
    val elevation = 5.dp
    val dialogColor = LocalElevationOverlay.current?.apply(
        color, elevation
    ) ?: color

    AlertDialog(
        backgroundColor = dialogColor,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmRequest) {
                Text(
                    text = confirmButtonText,
                    style = MaterialTheme.typography.body1
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = dismissButtonText,
                    style = MaterialTheme.typography.body1
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h6
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.body1
            )
        }
    )
}