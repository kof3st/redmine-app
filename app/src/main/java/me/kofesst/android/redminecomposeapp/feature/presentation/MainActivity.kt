package me.kofesst.android.redminecomposeapp.feature.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
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
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = backStackEntry?.destination?.route
                    var currentScreen by remember { mutableStateOf<Screen>(Screen.Auth) }
                    currentScreen = Screen.all.firstOrNull { screen ->
                        screen.route == currentRoute
                    } ?: Screen.Auth

                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            TopBar(
                                currentScreen = currentScreen,
                                navController = navController
                            )
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                currentScreen = currentScreen,
                                navController = navController
                            )
                        }
                    ) {
                        ScreenNavHost(
                            navController = navController,
                            paddingValues = it
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    currentScreen: Screen,
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = stringResource(currentScreen.nameRes)) },
        navigationIcon = if (currentScreen.hasBackButton) {
            {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        } else {
            null
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ScreenNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Screen.all.forEach { screen ->
            composable(
                route = screen.route,
                arguments = screen.args
            ) { entry ->
                screen.getContent(
                    navController = navController,
                    navBackStackEntry = entry
                )()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    navController: NavController
) {
    AnimatedVisibility(
        visible = currentScreen.hasBottomBar,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        BottomNavigation(elevation = 5.dp) {
            Screen.bottomBarScreens.forEach { screen ->
                val selected = screen.route == currentScreen.route
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