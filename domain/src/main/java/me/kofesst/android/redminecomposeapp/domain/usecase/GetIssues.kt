package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.model.ItemsPage
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class GetIssues(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(
        projectId: Int? = null,
        trackerId: Int? = null,
        statusId: Int? = null,
        sortState: String? = null,
        offset: Int = 0,
        limit: Int = 25,
    ): ItemsPage<Issue> {
        return repository.getIssues(
            projectId = projectId,
            trackerId = trackerId,
            statusId = statusId,
            sortState = sortState,
            offset = offset,
            limit = limit
        )
    }
}