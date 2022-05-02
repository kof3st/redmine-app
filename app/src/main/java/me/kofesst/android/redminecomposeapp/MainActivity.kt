package me.kofesst.android.redminecomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import me.kofesst.android.redminecomposeapp.feature.presentation.Screen
import me.kofesst.android.redminecomposeapp.feature.presentation.auth.AuthScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.issues.IssuesScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.project.ProjectsScreen
import me.kofesst.android.redminecomposeapp.ui.theme.RedmineComposeAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedmineComposeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scaffoldState = rememberScaffoldState()
                    val navController = rememberNavController()

                    Scaffold(
                        scaffoldState = scaffoldState,
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    ) {
                        ScreenNavHost(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(route = Screen.Auth.route) {
            AuthScreen(navController = navController)
        }
        composable(route = Screen.Issues.route) {
            IssuesScreen()
        }
        composable(route = Screen.Projects.route) {
            ProjectsScreen()
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBar = Screen.bottomBarScreens.any { it.route == currentRoute }

    AnimatedVisibility(
        visible = showBar,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        BottomNavigation(
            modifier = modifier,
            elevation = 5.dp
        ) {
            Screen.bottomBarScreens.forEach { screen ->
                val selected = screen.route == currentRoute
                BottomNavigationItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route)
                    },
                    icon = {
                        BottomNavigationItemContent(
                            selected = selected,
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = null
                                )
                            },
                            text = stringResource(screen.nameRes)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationItemContent(
    selected: Boolean,
    icon: @Composable ColumnScope.() -> Unit,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        AnimatedVisibility(visible = selected) {
            Text(
                text = text,
                fontSize = 12.sp
            )
        }
    }
}