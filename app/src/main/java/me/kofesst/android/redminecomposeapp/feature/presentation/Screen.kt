package me.kofesst.android.redminecomposeapp.feature.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import me.kofesst.android.redminecomposeapp.R

sealed class Screen(
    route: String,
    val args: List<NamedNavArgument> = listOf(),
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int = 0,
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

    object Auth : Screen(
        route = "auth-screen",
        nameRes = R.string.auth
    )

    object Issues : Screen(
        route = "issues-screen",
        nameRes = R.string.issues,
        iconRes = R.drawable.ic_issues_24,
        hasBottomBar = true
    )

    object Issue : Screen(
        route = "issue-screen",
        nameRes = R.string.issue_details,
        hasBackButton = true,
        args = listOf(
            navArgument("issueId") {
                type = NavType.IntType
                nullable = false
            }
        )
    )

    object Projects : Screen(
        route = "projects-screen",
        nameRes = R.string.projects,
        iconRes = R.drawable.ic_projects_24,
        hasBottomBar = true
    )

    object Project : Screen(
        route = "project-screen",
        nameRes = R.string.project_details,
        hasBackButton = true,
        args = listOf(
            navArgument("projectId") {
                type = NavType.IntType
                nullable = false
            }
        )
    )
}
