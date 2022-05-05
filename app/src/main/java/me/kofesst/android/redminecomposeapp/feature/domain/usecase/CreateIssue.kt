package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.CreateIssueBody
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository

class CreateIssue(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(issue: CreateIssueBody) {
        repository.createIssue(issue)
    }
}