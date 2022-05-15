package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.IssuesResponse
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository

class GetProjectIssues(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(projectId: Int, offset: Int = 0): IssuesResponse {
        return repository.getProjectIssues(
            projectId = projectId,
            offset = offset
        )
    }
}