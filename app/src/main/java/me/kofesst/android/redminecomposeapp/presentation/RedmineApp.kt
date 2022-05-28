@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package me.kofesst.android.redminecomposeapp.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import me.kofesst.android.redminecomposeapp.presentation.accounts.edit.CreateEditAccountScreen
import me.kofesst.android.redminecomposeapp.presentation.accounts.list.AccountsScreen
import me.kofesst.android.redminecomposeapp.presentation.auth.AuthScreen
import me.kofesst.android.redminecomposeapp.presentation.issue.edit.CreateEditIssueScreen
import me.kofesst.android.redminecomposeapp.presentation.issue.item.IssueScreen
import me.kofesst.android.redminecomposeapp.presentation.issue.list.IssuesScreen
import me.kofesst.android.redminecomposeapp.presentation.project.item.ProjectScreen
import me.kofesst.android.redminecomposeapp.presentation.project.list.ProjectsScreen
import me.kofesst.android.redminecomposeapp.presentation.util.activity

val LocalAppState = compositionLocalOf<RedmineAppState> {
    error("App state did not initialized")
}

@Composable
fun RedmineApp() {
    val appState = LocalAppState.current

    val viewModel: MainViewModel = hiltViewModel()

    val currentUser by viewModel.userHolder.currentUser.observeAsState()
    val isSignedIn = currentUser != null

    val navController = appState.navController
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Auth) }
    currentScreen = Screen.all.firstOrNull { screen ->
        screen.route == currentRoute
    } ?: Screen.Auth

    Scaffold(
        scaffoldState = appState.scaffoldState,
        topBar = {
            TopBar(
                currentScreen = currentScreen,
                navController = navController,
                isSignedIn = isSignedIn,
                onSessionClearClick = {
                    viewModel.clearSession {
                        navController.navigate(Screen.Auth.route)
                    }
                }
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TopBar(
    currentScreen: Screen,
    navController: NavController,
    isSignedIn: Boolean,
    onSessionClearClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TopAppBar(
        title = { Text(text = stringResource(currentScreen.nameRes)) },
        navigationIcon = if (currentScreen.hasBackButton) {
            {
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        navController.navigateUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        } else {
            null
        },
        actions = {
            if (isSignedIn) {
                IconButton(onClick = onSessionClearClick) {
                    Icon(
                        imageVector = Icons.Outlined.ExitToApp,
                        contentDescription = "Sign out"
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ScreenNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    val context = LocalContext.current
    val activity = context.activity ?: return

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        composable(route = Screen.Auth.route) {
            AuthScreen()
        }
        composable(route = Screen.Issues.route) {
            IssuesScreen(
                viewModel = hiltViewModel(viewModelStoreOwner = activity)
            )
        }
        composable(
            route = Screen.Issue.route,
            arguments = Screen.Issue.args
        ) { entry ->
            IssueScreen(
                issueId = entry.arguments?.getInt("issueId") ?: -1
            )
        }
        composable(
            route = Screen.CreateEditIssue.route,
            arguments = Screen.CreateEditIssue.args
        ) { entry ->
            CreateEditIssueScreen(
                issueId = entry.arguments?.getInt("issueId") ?: -1,
                projectId = entry.arguments?.getInt("projectId") ?: -1
            )
        }
        composable(route = Screen.Projects.route) {
            ProjectsScreen(
                viewModel = hiltViewModel(viewModelStoreOwner = activity)
            )
        }
        composable(
            route = Screen.Project.route,
            arguments = Screen.Project.args
        ) { entry ->
            ProjectScreen(
                projectId = entry.arguments?.getInt("projectId") ?: -1
            )
        }
        composable(route = Screen.Accounts.route) {
            AccountsScreen(
                viewModel = hiltViewModel(viewModelStoreOwner = activity)
            )
        }
        composable(
            route = Screen.CreateEditAccount.route,
            arguments = Screen.CreateEditAccount.args
        ) { entry ->
            CreateEditAccountScreen(
                accountId = entry.arguments?.getInt("accountId") ?: -1
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    navController: NavController,
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
                        navController.navigate(screen.route) {
                            if (screen.hasBottomBar) {
                                popUpToTop(navController)
                            }
                        }
                    },
                    icon = {
                        BottomNavigationItemContent(
                            selected = selected,
                            icon = {
                                Icon(
                                    painter = painterResource(screen.iconRes),
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
    text: String,
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

fun NavOptionsBuilder.popUpToTop(navController: NavController) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = true
    }
}