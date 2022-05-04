package me.kofesst.android.redminecomposeapp.feature.presentation.issue

import me.kofesst.android.redminecomposeapp.feature.domain.util.IssueFilterState
import me.kofesst.android.redminecomposeapp.feature.domain.util.IssueSortState

sealed class SortFilterEvent {
    data class SortStateChanged(val sortState: IssueSortState) : SortFilterEvent()
    data class FilterStateChanged(val filterState: IssueFilterState?) : SortFilterEvent()
}