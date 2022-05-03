package me.kofesst.android.redminecomposeapp.feature.data.model.issue

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue

data class IssuesResponse(
    val issues: List<Issue>,
    val limit: Int,
    val offset: Int,
    val total_count: Int
)