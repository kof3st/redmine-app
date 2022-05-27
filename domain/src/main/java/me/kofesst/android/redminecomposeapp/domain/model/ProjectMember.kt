package me.kofesst.android.redminecomposeapp.domain.model

data class ProjectMember(
    val id: Int,
    val projectId: Int,
    val user: IdName
)
