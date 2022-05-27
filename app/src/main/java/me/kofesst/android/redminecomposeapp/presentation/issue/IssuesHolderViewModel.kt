package me.kofesst.android.redminecomposeapp.presentation.issue

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.presentation.ViewModelBase
import me.kofesst.android.redminecomposeapp.presentation.util.IssueFilterState
import me.kofesst.android.redminecomposeapp.presentation.util.IssueSortState
import me.kofesst.android.redminecomposeapp.presentation.util.OrderType

abstract class IssuesHolderViewModel(
    protected val useCases: UseCases,
) : ViewModelBase() {
    protected abstract val source: List<Issue>

    private val _filteredIssues = MutableStateFlow<List<Issue>>(listOf())
    val issues get() = _filteredIssues.asStateFlow()

    private val _trackers = MutableStateFlow<List<IdName>>(listOf())
    val trackers get() = _trackers.asStateFlow()

    private val _statuses = MutableStateFlow<List<IdName>>(listOf())
    val statuses get() = _statuses.asStateFlow()

    private val _filterState = mutableStateOf<IssueFilterState?>(null)
    val filterState: State<IssueFilterState?> get() = _filterState

    private val _sortState = mutableStateOf<IssueSortState>(IssueSortState.default)
    val sortState: State<IssueSortState> get() = _sortState

    protected suspend fun loadFilterValues() {
        _trackers.value = useCases.getTrackers()
        _statuses.value = useCases.getStatuses()
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
                        source.sortedBy { it.id }
                    }
                    is OrderType.Descending -> {
                        source.sortedByDescending { it.id }
                    }
                }
            }
            is IssueSortState.ByPriority -> {
                when (sortState.value.orderType) {
                    is OrderType.Ascending -> {
                        source.sortedBy { it.priority.id }
                    }
                    is OrderType.Descending -> {
                        source.sortedByDescending { it.priority.id }
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