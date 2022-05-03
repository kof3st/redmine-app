package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository

class GetIssueDetails(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(issueId: Int): Issue {
        return repository.getIssueDetails(issueId)
    }
}