package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.model.UploadData
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class CreateIssue(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(issue: Issue, attachments: List<UploadData>) {
        repository.createIssue(issue, attachments)
    }
}