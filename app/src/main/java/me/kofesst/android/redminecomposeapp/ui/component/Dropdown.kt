@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package me.kofesst.android.redminecomposeapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RedmineDropdown(
    items: List<DropdownItem>,
    value: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    errorMessage: String? = null,
    hasNullValue: Boolean = false,
    onNullValueSelected: () -> Unit = {},
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
            isError = errorMessage != null,
            label = { Text(text = placeholder) },
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
            items.let {
                if (hasNullValue) {
                    it + DropdownItem.getNullItem(onNullValueSelected)
                } else {
                    it
                }
            }.forEach { item ->
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
    TextFieldError(
        modifier = Modifier.fillMaxWidth(),
        message = errorMessage
    )
}

data class DropdownItem(
    val text: String,
    val onSelected: () -> Unit,
) {
    companion object {
        fun getNullItem(onSelected: () -> Unit): DropdownItem {
            return DropdownItem("Нет", onSelected)
        }
    }
}