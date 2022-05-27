package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.ProjectMember
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class GetMembers(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(projectId: Int): List<ProjectMember> {
        return repository.getMembers(projectId)
    }
}