package me.kofesst.android.redminecomposeapp.presentation.issue

import me.kofesst.android.redminecomposeapp.presentation.util.IssueFilterState
import me.kofesst.android.redminecomposeapp.presentation.util.IssueSortState

sealed class SortFilterEvent {
    data class SortStateChanged(val sortState: IssueSortState) : SortFilterEvent()
    data class FilterStateChanged(val filterState: IssueFilterState?) : SortFilterEvent()
}