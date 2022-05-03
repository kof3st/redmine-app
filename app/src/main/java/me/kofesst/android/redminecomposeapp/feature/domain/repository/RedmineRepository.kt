package me.kofesst.android.redminecomposeapp.feature.domain.repository

import me.kofesst.android.redminecomposeapp.feature.data.model.project.Project
import me.kofesst.android.redminecomposeapp.feature.domain.model.CurrentUser

interface RedmineRepository {

    suspend fun getCurrentUser(host: String, apiKey: String): CurrentUser

    suspend fun getProjects(): List<Project>
}