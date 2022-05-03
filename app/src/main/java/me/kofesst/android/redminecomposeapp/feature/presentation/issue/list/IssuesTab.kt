package me.kofesst.android.redminecomposeapp.feature.presentation.issue.list

import androidx.annotation.StringRes
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue

sealed class IssuesTab(@StringRes val titleRes: Int, val index: Int) {
    abstract fun filterIssues(userId: Int, issues: List<Issue>): List<Issue>

    object Assigned : IssuesTab(R.string.assigned_to_me, 0) {
        override fun filterIssues(userId: Int, issues: List<Issue>): List<Issue> =
            issues.filter { it.assigned_to?.id == userId }
    }

    object Owned : IssuesTab(R.string.owned_issues, 1) {
        override fun filterIssues(userId: Int, issues: List<Issue>): List<Issue> =
            issues.filter { it.author.id == userId }
    }
}
