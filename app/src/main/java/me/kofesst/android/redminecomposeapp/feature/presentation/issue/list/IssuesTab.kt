package me.kofesst.android.redminecomposeapp.feature.presentation.issue.list

import androidx.annotation.StringRes
import me.kofesst.android.redminecomposeapp.R

sealed class IssuesTab(@StringRes val titleRes: Int, val index: Int) {
    object Assigned : IssuesTab(R.string.assigned_to_me, 0)
    object Owned : IssuesTab(R.string.owned_issues, 1)
}
