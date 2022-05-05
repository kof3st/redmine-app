package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.CreateIssueBody
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository

class UpdateIssue(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(
        issueId: Int,
        issue: CreateIssueBody
    ) {
        repository.updateIssue(issueId, issue)
    }
}