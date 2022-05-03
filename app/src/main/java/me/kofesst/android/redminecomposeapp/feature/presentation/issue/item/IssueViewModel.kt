package me.kofesst.android.redminecomposeapp.feature.presentation.issue.item

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.presentation.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class IssueViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModelBase() {
    private val _issue = MutableStateFlow<Issue?>(null)
    val issue get() = _issue.asStateFlow()

    fun refreshData(issueId: Int) {
        startLoading {
            _issue.value = useCases.getIssueDetails(issueId)
        }
    }
}