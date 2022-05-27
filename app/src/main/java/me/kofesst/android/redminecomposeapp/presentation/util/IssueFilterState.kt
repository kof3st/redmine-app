package me.kofesst.android.redminecomposeapp.presentation.util

import androidx.annotation.StringRes
import me.kofesst.android.redminecomposeapp.R
import me.kofesst.android.redminecomposeapp.domain.model.IdName

sealed class IssueFilterState(
    @StringRes val nameRes: Int,
    val item: Any?,
) {
    data class ByTracker(val tracker: IdName? = null) :
        IssueFilterState(R.string.filter_by_tracker, tracker)

    data class ByStatus(val status: IdName? = null) :
        IssueFilterState(R.string.filter_by_status, status)
}
