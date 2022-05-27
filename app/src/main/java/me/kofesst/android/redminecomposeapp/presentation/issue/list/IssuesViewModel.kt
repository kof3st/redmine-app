package me.kofesst.android.redminecomposeapp.presentation.issue.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.model.ItemsPage
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.presentation.issue.IssuesHolderViewModel
import javax.inject.Inject

@HiltViewModel
class IssuesViewModel @Inject constructor(
    useCases: UseCases,
) : IssuesHolderViewModel(useCases) {
    private val _ownedIssues = MutableStateFlow<ItemsPage<Issue>?>(null)
    private val _assignedIssues = MutableStateFlow<ItemsPage<Issue>?>(null)

    private val _currentTab = mutableStateOf<IssuesTab>(IssuesTab.Assigned)
    val currentTab: State<IssuesTab> get() = _currentTab

    private val _issues = MutableStateFlow<ItemsPage<Issue>?>(null)

    val shouldLoadMore: Boolean
        get() = _issues.value?.let {
            val total = it.totalCount
            it.items.size < total
        } ?: true

    override val source: List<Issue>
        get() = _issues.value?.items ?: listOf()

    fun loadNextPage() {
        startLoading {
            val offset = _issues.value?.items?.size ?: 0
            when (currentTab.value) {
                is IssuesTab.Assigned -> {
                    val nextPage = useCases.getAssignedIssues(offset)
                    _assignedIssues.value = _assignedIssues.value?.let { assigned ->
                        assigned.copy(
                            items = assigned.items + nextPage.items,
                            totalCount = nextPage.totalCount
                        )
                    } ?: nextPage
                }
                is IssuesTab.Owned -> {
                    val nextPage = useCases.getOwnedIssues(offset)
                    _ownedIssues.value = _ownedIssues.value?.let { owned ->
                        owned.copy(
                            items = owned.items + nextPage.items,
                            totalCount = nextPage.totalCount
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