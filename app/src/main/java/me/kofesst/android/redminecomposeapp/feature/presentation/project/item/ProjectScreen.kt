package me.kofesst.android.redminecomposeapp.feature.presentation.project.item

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ProjectScreen(
    projectId: Int,
    navController: NavController
) {
    Text(text = projectId.toString())
}