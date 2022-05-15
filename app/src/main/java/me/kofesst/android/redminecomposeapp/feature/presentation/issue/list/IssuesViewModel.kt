package me.kofesst.android.redminecomposeapp.feature.presentation.issue.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.IssuesResponse
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.IssuesHolderViewModel
import javax.inject.Inject

@HiltViewModel
class IssuesViewModel @Inject constructor(
    useCases: UseCases,
) : IssuesHolderViewModel(useCases) {
    private val _ownedIssues = MutableStateFlow<IssuesResponse?>(null)
    private val _assignedIssues = MutableStateFlow<IssuesResponse?>(null)

    private val _currentTab = mutableStateOf<IssuesTab>(IssuesTab.Assigned)
    val currentTab: State<IssuesTab> get() = _currentTab

    private val _issues = MutableStateFlow<IssuesResponse?>(null)

    val shouldLoadMore: Boolean
        get() = _issues.value?.let {
            val total = it.total_count
            it.issues.size < total
        } ?: true

    override val source: List<Issue>
        get() = _issues.value?.issues ?: listOf()

    fun loadNextPage() {
        startLoading {
            val offset = _issues.value?.issues?.size ?: 0
            when (currentTab.value) {
                is IssuesTab.Assigned -> {
                    val nextPage = useCases.getAssignedIssues(offset)
                    _assignedIssues.value = _assignedIssues.value?.let { assigned ->
                        assigned.copy(
                            issues = assigned.issues + nextPage.issues,
                            total_count = nextPage.total_count
                        )
                    } ?: nextPage
                }
                is IssuesTab.Owned -> {
                    val nextPage = useCases.getOwnedIssues(offset)
                    _ownedIssues.value = _ownedIssues.value?.let { owned ->
                        owned.copy(
                            issues = owned.issues + nextPage.issues,
                            total_count = nextPage.total_count
                        )
                    } ?: nextPage
                }
            }
            updateTabIssues()
        }
    }

    fun refreshData() {
        startLoading {
            loadFilterValues()
            updateIssues()
            updateTabIssues()
        }
    }

    fun onTabSelected(tab: IssuesTab) {
        _currentTab.value = tab
        updateTabIssues()
    }

    private suspend fun updateIssues() {
        _ownedIssues.value = useCases.getOwnedIssues()
        _assignedIssues.value = useCases.getAssignedIssues()
    }

    private fun updateTabIssues() {
        _issues.value = when (currentTab.value) {
            is IssuesTab.Assigned -> _assignedIssues.value
            is IssuesTab.Owned -> _ownedIssues.value
        }
        sortFilterIssues()
    }
}