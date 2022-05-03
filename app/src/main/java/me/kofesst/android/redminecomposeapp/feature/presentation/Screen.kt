package me.kofesst.android.redminecomposeapp.feature.presentation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.*
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.feature.presentation.auth.AuthScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.item.IssueScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.list.IssuesScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.project.item.ProjectScreen
import me.kofesst.android.redminecomposeapp.feature.presentation.project.list.ProjectsScreen

sealed class Screen(
    route: String,
    val args: List<NamedNavArgument> = listOf(),
    @StringRes val nameRes: Int,
    val icon: ImageVector,
    val hasBottomBar: Boolean = false,
    val hasBackButton: Boolean = false
) {
    companion object {
        val all
            get() = listOf(
                Auth,
                Issues,
                Issue,
                Projects,
                Project
            )

        val bottomBarScreens
            get() = all.filter { it.hasBottomBar }
    }

    var route: String = buildString {
        append(route)
        args.filter { !it.argument.isNullable }.forEach {
            append("/{${it.name}}")
        }

        args.filter { it.argument.isNullable }.also {
            if (it.isEmpty()) return@also

            append(
                "?${
                    it.joinToString("&") { arg ->
                        "${arg.name}={${arg.name}}"
                    }
                }"
            )
        }
    }

    fun withArgs(vararg args: Pair<String, Any>): String {
        return args.fold(route) { acc, (key, value) ->
            acc
                .replace("{$key}", value.toString(), true)
                .replace(key, value.toString(), true)
        }
    }

    abstract fun getContent(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ): @Composable () -> Unit

    object Auth : Screen(
        route = "auth-screen",
        nameRes = R.string.auth,
        icon = Icons.Outlined.AccountCircle
    ) {
        override fun getContent(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ): @Composable () -> Unit {
            return { AuthScreen(navController = navController) }
        }
    }

    object Issues : Screen(
        route = "issues-screen",
        nameRes = R.string.issues,
        icon = Icons.Outlined.List,
        hasBottomBar = true
    ) {
        override fun getContent(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ): @Composable () -> Unit {
            return { IssuesScreen(navController = navController) }
        }
    }

    object Issue : Screen(
        route = "issue-screen",
        nameRes = R.string.issue_details,
        icon = Icons.Outlined.List,
        hasBackButton = true,
        args = listOf(
            navArgument("issueId") {
                type = NavType.IntType
                nullable = false
            }
        )
    ) {
        override fun getContent(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ): @Composable () -> Unit {
            return {
                IssueScreen(
                    issueId = navBackStackEntry.arguments?.getInt("issueId") ?: -1,
                    navController = navController
                )
            }
        }
    }

    object Projects : Screen(
        route = "projects-screen",
        nameRes = R.string.projects,
        icon = Icons.Outlined.Person,
        hasBottomBar = true
    ) {
        override fun getContent(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ): @Composable () -> Unit {
            return { ProjectsScreen(navController = navController) }
        }
    }

    object Project : Screen(
        route = "project-screen",
        nameRes = R.string.project_details,
        icon = Icons.Outlined.Person,
        hasBackButton = true,
        args = listOf(
            navArgument("projectId") {
                type = NavType.IntType
                nullable = false
            }
        )
    ) {
        override fun getContent(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ): @Composable () -> Unit {
            return {
                ProjectScreen(
                    projectId = navBackStackEntry.arguments?.getInt("projectId") ?: -1,
                    navController = navController
                )
            }
        }
    }
}
