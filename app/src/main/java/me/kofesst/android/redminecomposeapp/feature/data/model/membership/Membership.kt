package me.kofesst.android.redminecomposeapp.feature.data.model.membership

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Project

data class Membership(
    val id: Int,
    val project: Project,
    val roles: List<Role>,
    val user: User
)