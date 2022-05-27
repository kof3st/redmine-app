package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.model.ItemsPage
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class GetAssignedIssues(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(offset: Int = 0): ItemsPage<Issue> {
        return repository.getAssignedIssues(offset)
    }
}