package me.kofesst.android.redminecomposeapp.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.kofesst.android.redminecomposeapp.R

@Composable
fun SourceHandler(
    emptySourceIcon: Painter = painterResource(id = R.drawable.ic_empty_source_24),
    @StringRes emptySourceTextRes: Int = R.string.empty_source,
    source: List<Any> = listOf<Int>(),
    content: @Composable () -> Unit = {},
) {
    if (source.isEmpty()) {
        EmptySourceAlert(
            icon = emptySourceIcon,
            textRes = emptySourceTextRes,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        content()
    }
}

@Composable
fun EmptySourceAlert(
    icon: Painter,
    textRes: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.h6
        )
    }
}