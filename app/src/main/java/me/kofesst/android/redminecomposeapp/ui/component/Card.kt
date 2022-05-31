package me.kofesst.android.redminecomposeapp.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RedmineCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 5.dp,
    cornerRadius: Dp = 5.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var cardModifier = modifier
    if (onClick != null) {
        cardModifier = cardModifier.clickable { onClick() }
    }

    Card(
        elevation = elevation,
        shape = RoundedCornerShape(cornerRadius),
        modifier = cardModifier,
        content = content
    )
}