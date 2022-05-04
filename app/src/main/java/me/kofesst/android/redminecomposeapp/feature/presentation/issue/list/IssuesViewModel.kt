package me.kofesst.android.redminecomposeapp.feature.presentation.issue.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.domain.util.UserHolder
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.IssuesHolderViewModel
import javax.inject.Inject

@HiltViewModel
class IssuesViewModel @Inject constructor(
    useCases: UseCases,
    private val userHolder: UserHolder
) : IssuesHolderViewModel(useCases) {
    private val _issues = MutableStateFlow<List<Issue>>(listOf())

    private val _currentTab = mutableStateOf<IssuesTab>(IssuesTab.Assigned)
    val currentTab: State<IssuesTab> get() = _currentTab

    private val _tabIssues = MutableStateFlow<List<Issue>>(listOf())
    override val sourceIssues: MutableStateFlow<List<Issue>>
        get() = _tabIssues

    fun refreshData() {
        startLoading {
            loadFilterValues()
            _issues.value = useCases.getIssues()
            filterIssues()
            sortFilterIssues()
        }
    }

    fun onTabSelected(tab: IssuesTab) {
        _currentTab.value = tab
        filterIssues()
        sortFilterIssues()
    }

    private fun filterIssues() {
        val userId = userHolder.requireUser().id
        _tabIssues.value = currentTab.value.filterIssues(userId, _issues.value)
    }
}