package me.kofesst.android.redminecomposeapp.feature.presentation.project.item

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.IssuesResponse
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.presentation.issue.IssuesHolderViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    useCases: UseCases,
) : IssuesHolderViewModel(useCases) {
    private val _issues = MutableStateFlow<IssuesResponse?>(null)

    val shouldLoadMore: Boolean
        get() = _issues.value?.let {
            val total = it.total_count
            it.issues.size < total
        } ?: true

    override val source: List<Issue>
        get() = _issues.value?.issues ?: listOf()

    fun loadNextPage(projectId: Int) {
        startLoading {
            val offset = _issues.value?.issues?.size ?: 0
            val nextPage = useCases.getProjectIssues(
                projectId = projectId,
                offset = offset
            )

            _issues.value = _issues.value?.let { loaded ->
                loaded.copy(
                    issues = loaded.issues + nextPage.issues,
                    total_count = nextPage.total_count
                )
            } ?: nextPage
            sortFilterIssues()
        }
    }

    fun refreshData(projectId: Int) {
        startLoading {
            loadFilterValues()
            _issues.value = useCases.getProjectIssues(projectId = projectId)
            sortFilterIssues()
        }
    }
}