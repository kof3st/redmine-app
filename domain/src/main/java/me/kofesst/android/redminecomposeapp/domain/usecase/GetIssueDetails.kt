package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class GetIssueDetails(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(issueId: Int): Issue {
        return repository.getIssueDetails(issueId)
    }
}