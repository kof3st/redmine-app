package me.kofesst.android.redminecomposeapp.feature.domain.util

import androidx.annotation.StringRes
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Status
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Tracker

sealed class IssueFilterState(
    @StringRes val nameRes: Int,
    val item: Any?
) {
    data class ByTracker(val tracker: Tracker? = null) :
        IssueFilterState(R.string.filter_by_tracker, tracker)

    data class ByStatus(val status: Status? = null) :
        IssueFilterState(R.string.filter_by_status, status)
}
