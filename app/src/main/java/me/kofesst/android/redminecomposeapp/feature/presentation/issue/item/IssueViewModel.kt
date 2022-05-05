package me.kofesst.android.redminecomposeapp.feature.presentation.issue.item

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Priority
import me.kofesst.android.redminecomposeapp.feature.data.model.status.Status
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.presentation.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class IssueViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModelBase() {
    private val _issue = MutableStateFlow<Issue?>(null)
    val issue get() = _issue.asStateFlow()

    private val _statuses = MutableStateFlow<List<Status>>(listOf())
    val statuses get() = _statuses.asStateFlow()

    private val _priorities = MutableStateFlow(Priority.priorities)
    val priorities get() = _priorities.asStateFlow()

    fun refreshData(issueId: Int) {
        startLoading {
            _statuses.value = useCases.getStatuses()
            _issue.value = useCases.getIssueDetails(issueId)
        }
    }
}