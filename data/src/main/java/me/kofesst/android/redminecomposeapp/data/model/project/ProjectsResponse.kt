package me.kofesst.android.redminecomposeapp.data.model.project

data class ProjectsResponse(
    val limit: Int,
    val offset: Int,
    val projects: List<ProjectDto>,
    val total_count: Int,
)