package me.kofesst.android.redminecomposeapp.feature.presentation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import me.kofesst.android.redminecomposeapp.R

// Pair#first - ключ аргумента
// Pair#second - обязательный ли аргумент
typealias ScreenArg = Pair<String, Boolean>

sealed class Screen(
    route: String,
    @StringRes val nameRes: Int,
    val icon: ImageVector,
    val showScaffold: Boolean = false,
    args: List<ScreenArg> = listOf()
) {
    companion object {
        val bottomBarScreens
            get() = listOf(
                Issues,
                Projects
            )
    }

    var route: String = buildString {
        append(route)
        args.filter { it.second }.forEach {
            append("/${it.first}")
        }

        args.filter { !it.second }.also {
            if (it.isEmpty()) return@also

            append(
                "?${
                    it.joinToString("&") { arg ->
                        "${arg.first}={${arg.first}}"
                    }
                }"
            )
        }
    }

    fun withArgs(vararg args: Pair<String, Any>): String {
        return args.fold(route) { acc, (key, value) ->
            acc.replace("{$key}", value.toString(), true)
        }
    }

    object Auth : Screen(
        route = "auth-screen",
        nameRes = R.string.auth,
        icon = Icons.Outlined.AccountCircle
    )

    object Issues : Screen(
        route = "issues-screen",
        nameRes = R.string.issues,
        icon = Icons.Outlined.List,
        showScaffold = true
    )

    object Projects : Screen(
        route = "projects-screen",
        nameRes = R.string.projects,
        icon = Icons.Outlined.Person,
        showScaffold = true
    )
}
