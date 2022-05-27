package me.kofesst.android.redminecomposeapp.presentation.issue.item

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.presentation.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class IssueViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModelBase() {
    private val _issue = MutableStateFlow<Issue?>(null)
    val issue get() = _issue.asStateFlow()

    private val _statuses = MutableStateFlow<List<IdName>>(listOf())
    val statuses get() = _statuses.asStateFlow()

    private val _priorities = MutableStateFlow(IdName.priorities)
    val priorities get() = _priorities.asStateFlow()

    private val _trackers = MutableStateFlow<List<IdName>>(listOf())
    val trackers get() = _trackers.asStateFlow()

    fun refreshData(issueId: Int) {
        startLoading {
            _statuses.value = useCases.getStatuses()
            _trackers.value = useCases.getTrackers()
            _issue.value = useCases.getIssueDetails(issueId)
        }
    }
}