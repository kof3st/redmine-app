package me.kofesst.android.redminecomposeapp.presentation

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Stable
class RedmineAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
)

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
) = RedmineAppState(
    scaffoldState = scaffoldState,
    navController = navController,
)