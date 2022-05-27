package me.kofesst.android.redminecomposeapp.data.model.issue

import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.model.ItemsPage

data class IssuesResponse(
    val issues: List<IssueDto>,
    val limit: Int,
    val offset: Int,
    val total_count: Int,
) {
    fun toPage() = ItemsPage<Issue>(
        items = issues.map { it.toIssue() },
        limit = limit,
        offset = offset,
        totalCount = total_count
    )
}