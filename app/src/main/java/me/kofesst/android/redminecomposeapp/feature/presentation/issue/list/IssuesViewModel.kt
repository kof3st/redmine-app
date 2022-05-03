package me.kofesst.android.redminecomposeapp.feature.presentation.issue.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.domain.util.UserHolder
import me.kofesst.android.redminecomposeapp.feature.presentation.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class IssuesViewModel @Inject constructor(
    private val useCases: UseCases,
    private val userHolder: UserHolder
) : ViewModelBase() {
    private val issues = MutableStateFlow<List<Issue>>(listOf())

    private val _currentTab = mutableStateOf<IssuesTab>(IssuesTab.Assigned)
    val currentTab: State<IssuesTab> get() = _currentTab

    private val _tabIssues = MutableStateFlow<List<Issue>>(listOf())
    val tabIssues get() = _tabIssues.asStateFlow()

    fun refreshData() {
        startLoading {
            issues.value = useCases.getIssues()
            filterIssues()
        }
    }

    fun onTabSelected(tab: IssuesTab) {
        _currentTab.value = tab
        filterIssues()
    }

    private fun filterIssues() {
        val userId = userHolder.requireUser().id
        _tabIssues.value = currentTab.value.filterIssues(userId, issues.value)
    }
}