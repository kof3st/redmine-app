package me.kofesst.android.redmineapp.feature.data.model.project

import me.kofesst.android.redminecomposeapp.feature.data.model.project.Project

data class ProjectsResponse(
    val limit: Int,
    val offset: Int,
    val projects: List<Project>,
    val total_count: Int
)