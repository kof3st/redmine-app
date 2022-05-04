package me.kofesst.android.redminecomposeapp.feature.presentation.project.item

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.IssuesHolderViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    useCases: UseCases
) : IssuesHolderViewModel(useCases) {
    private val _issues = MutableStateFlow<List<Issue>>(listOf())
    override val sourceIssues: MutableStateFlow<List<Issue>>
        get() = _issues

    fun refreshData(projectId: Int) {
        startLoading {
            loadFilterValues()
            _issues.value = useCases.getIssues().filter { issue ->
                issue.project.id == projectId
            }
            sortFilterIssues()
        }
    }
}