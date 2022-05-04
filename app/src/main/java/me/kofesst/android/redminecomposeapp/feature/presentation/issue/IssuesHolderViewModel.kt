package me.kofesst.android.redminecomposeapp.feature.presentation.issue

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Status
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Tracker
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.domain.util.IssueFilterState
import me.kofesst.android.redminecomposeapp.feature.domain.util.IssueSortState
import me.kofesst.android.redminecomposeapp.feature.domain.util.OrderType
import me.kofesst.android.redminecomposeapp.feature.presentation.ViewModelBase

abstract class IssuesHolderViewModel(
    protected val useCases: UseCases
) : ViewModelBase() {
    protected abstract val sourceIssues: MutableStateFlow<List<Issue>>

    private val _filteredIssues = MutableStateFlow<List<Issue>>(listOf())
    val issues get() = _filteredIssues.asStateFlow()

    private val _trackers = MutableStateFlow<List<Tracker>>(listOf())
    val trackers get() = _trackers.asStateFlow()

    private val _statuses = MutableStateFlow<List<Status>>(listOf())
    val statuses get() = _statuses.asStateFlow()

    private val _filterState = mutableStateOf<IssueFilterState?>(null)
    val filterState: State<IssueFilterState?> get() = _filterState

    private val _sortState = mutableStateOf<IssueSortState>(IssueSortState.default)
    val sortState: State<IssueSortState> get() = _sortState

    protected suspend fun loadFilterValues() {
        _trackers.value = useCases.getTrackers()
    }

    fun onSortFilterEvent(event: SortFilterEvent) {
        when (event) {
            is SortFilterEvent.SortStateChanged -> {
                _sortState.value = event.sortState
            }
            is SortFilterEvent.FilterStateChanged -> {
                _filterState.value = event.filterState
            }
        }
        sortFilterIssues()
    }

    protected fun sortFilterIssues() {
        var filtered = when (sortState.value) {
            is IssueSortState.ById -> {
                when (sortState.value.orderType) {
                    is OrderType.Ascending -> {
                        sourceIssues.value.sortedBy { it.id }
                    }
                    is OrderType.Descending -> {
                        sourceIssues.value.sortedByDescending { it.id }
                    }
                }
            }
            is IssueSortState.ByPriority -> {
                when (sortState.value.orderType) {
                    is OrderType.Ascending -> {
                        sourceIssues.value.sortedBy { it.priority.id }
                    }
                    is OrderType.Descending -> {
                        sourceIssues.value.sortedByDescending { it.priority.id }
                    }
                }
            }
        }

        filterState.value?.also { state ->
            state.item?.also {
                filtered = when (state) {
                    is IssueFilterState.ByTracker -> {
                        filtered.filter { it.tracker.id == state.tracker!!.id }
                    }
                    is IssueFilterState.ByStatus -> {
                        filtered.filter { it.status.id == state.status!!.id }
                    }
                }
            }
        }

        _filteredIssues.value = filtered
    }
}