package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.project.Project
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository

class GetProjects(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(): List<Project> {
        return repository.getProjects()
    }
}