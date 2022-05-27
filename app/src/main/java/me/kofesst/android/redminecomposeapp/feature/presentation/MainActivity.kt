package me.kofesst.android.redminecomposeapp.feature.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import me.kofesst.android.redminecomposeapp.feature.presentation.accounts.edit.CreateEditAccountScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.accounts.list.AccountsScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.auth.AuthScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.edit.CreateEditIssueScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.item.IssueScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.list.IssuesScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.project.item.ProjectScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.project.list.ProjectsScreen
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
                    val viewModel: MainViewModel = hiltViewModel()
                    val isSignedIn = viewModel.userHolder.currentUser != null

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
            }
        }
    }

    @Composable
    fun TopBar(
        currentScreen: Screen,
        navController: NavController,
        isSignedIn: Boolean,
        onSessionClearClick: () -> Unit,
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
        NavHost(
            navController = navController,
            startDestination = Screen.Auth.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(route = Screen.Auth.route) {
                AuthScreen(navController = navController)
            }
            composable(route = Screen.Issues.route) {
                IssuesScreen(
                    navController = navController,
                    viewModel = hiltViewModel(viewModelStoreOwner = this@MainActivity)
                )
            }
            composable(
                route = Screen.Issue.route,
                arguments = Screen.Issue.args
            ) { entry ->
                IssueScreen(
                    issueId = entry.arguments?.getInt("issueId") ?: -1,
                    navController = navController
                )
            }
            composable(
                route = Screen.CreateEditIssue.route,
                arguments = Screen.CreateEditIssue.args
            ) { entry ->
                CreateEditIssueScreen(
                    issueId = entry.arguments?.getInt("issueId") ?: -1,
                    projectId = entry.arguments?.getInt("projectId") ?: -1,
                    navController = navController
                )
            }
            composable(route = Screen.Projects.route) {
                ProjectsScreen(
                    navController = navController,
                    viewModel = hiltViewModel(viewModelStoreOwner = this@MainActivity)
                )
            }
            composable(
                route = Screen.Project.route,
                arguments = Screen.Project.args
            ) { entry ->
                ProjectScreen(
                    projectId = entry.arguments?.getInt("projectId") ?: -1,
                    navController = navController
                )
            }
            composable(route = Screen.Accounts.route) {
                AccountsScreen(
                    navController = navController,
                    viewModel = hiltViewModel(viewModelStoreOwner = this@MainActivity)
                )
            }
            composable(
                route = Screen.CreateEditAccount.route,
                arguments = Screen.CreateEditAccount.args
            ) { entry ->
                CreateEditAccountScreen(
                    accountId = entry.arguments?.getInt("accountId") ?: -1,
                    navController = navController
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
}

fun NavOptionsBuilder.popUpToTop(navController: NavController) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive =  true
    }
}