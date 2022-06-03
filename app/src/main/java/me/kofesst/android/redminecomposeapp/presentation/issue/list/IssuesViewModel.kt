package me.kofesst.android.redminecomposeapp.presentation.issue.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.domain.util.UserHolder
import me.kofesst.android.redminecomposeapp.presentation.issue.IssuesHolderViewModel
import javax.inject.Inject

@HiltViewModel
class IssuesViewModel @Inject constructor(
    private val userHolder: UserHolder,
    useCases: UseCases,
) : IssuesHolderViewModel(useCases) {
    private val _currentTab = mutableStateOf<IssuesTab>(IssuesTab.Assigned)
    val currentTab: State<IssuesTab> get() = _currentTab

    private val _issues = MutableStateFlow<List<Issue>>(listOf())
    val issues get() = _issues.asStateFlow()

    fun refreshData() {
        startLoading {
            loadFilterValues()
            updateIssues()
            updateTabIssues()
        }
    }

    fun onTabSelected(tab: IssuesTab) {
        _currentTab.value = tab

        startLoading {
            updateIssues()
            updateTabIssues()
        }
    }

    private suspend fun updateIssues() {
        _issues.value = useCases.getIssues(
            trackerId = getFilterTrackerId(),
            statusId = getFilterStatusId(),
            sortState = getApiSortState(),
            limit = 100
        ).items
    }

    private fun updateTabIssues() {
        val userId = userHolder.requireUser().id

        _issues.value = _issues.value.let {
            when (currentTab.value) {
                is IssuesTab.Assigned -> {
                    it.filter { issue ->
                        when (issue.status.id) {
                            // Надо проверить & автор
                            3 -> issue.author.id == userId
                            // Надо сделать/в работе & исполнитель
                            in 1..2 -> issue.assignedTo?.id == userId
                            // Не включать задачу в список
                            else -> false
                        }
                    }
                }
                is IssuesTab.Owned -> {
                    it.filter { issue ->
                        issue.author.id == userId
                    }
                }
            }
        }
    }
}