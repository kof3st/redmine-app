package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class UpdateIssue(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(
        issueId: Int,
        issue: Issue,
        notes: String?,
    ) {
        repository.updateIssue(issueId, issue, notes)
    }
}