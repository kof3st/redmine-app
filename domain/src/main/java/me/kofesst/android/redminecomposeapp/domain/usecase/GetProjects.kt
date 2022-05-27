package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.Project
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class GetProjects(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(): List<Project> {
        return repository.getProjects()
    }
}