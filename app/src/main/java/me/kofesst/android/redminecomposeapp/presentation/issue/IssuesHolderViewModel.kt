package me.kofesst.android.redminecomposeapp.presentation.issue

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.presentation.ViewModelBase
import me.kofesst.android.redminecomposeapp.presentation.util.IssueFilterState
import me.kofesst.android.redminecomposeapp.presentation.util.IssueSortState

abstract class IssuesHolderViewModel(
    protected val useCases: UseCases,
) : ViewModelBase() {
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
    }

    protected fun getFilterTrackerId(): Int? =
        if (filterState.value is IssueFilterState.ByTracker) {
            getFilterStateTarget()?.id
        } else {
            null
        }

    protected fun getFilterStatusId(): Int? = if (filterState.value is IssueFilterState.ByStatus) {
        getFilterStateTarget()?.id
    } else {
        null
    }

    private fun getFilterStateTarget() = if (filterState.value!!.item == null) {
        null
    }
    else {
        filterState.value!!.item as IdName
    }

    protected fun getApiSortState() = sortState.value.toApiState()
}