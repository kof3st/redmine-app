package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.IssuesResponse
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository

class GetOwnedIssues(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(offset: Int = 0): IssuesResponse {
        return repository.getOwnedIssues(offset)
    }
}