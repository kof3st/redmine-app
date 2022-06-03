package me.kofesst.android.redminecomposeapp.presentation.project.item

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.domain.model.Issue
import me.kofesst.android.redminecomposeapp.domain.model.ItemsPage
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.presentation.issue.IssuesHolderViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    useCases: UseCases,
) : IssuesHolderViewModel(useCases) {
    private val _issues = MutableStateFlow<ItemsPage<Issue>?>(null)
    val issues get() = _issues.asStateFlow()

    val shouldLoadMore: Boolean
        get() = _issues.value?.let {
            val total = it.totalCount
            it.items.size < total
        } ?: true

    fun loadNextPage(projectId: Int) {
        startLoading {
            val offset = _issues.value?.items?.size ?: 0
            val nextPage = useCases.getIssues(
                projectId = projectId,
                trackerId = getFilterTrackerId(),
                statusId = getFilterStatusId(),
                sortState = getApiSortState(),
                offset = offset
            )

            _issues.value = _issues.value?.let { loaded ->
                loaded.copy(
                    items = loaded.items + nextPage.items,
                    totalCount = nextPage.totalCount
                )
            } ?: nextPage
        }
    }

    fun refreshData(projectId: Int) {
        startLoading {
            loadFilterValues()
            _issues.value = useCases.getIssues(
                projectId = projectId,
                trackerId = getFilterTrackerId(),
                statusId = getFilterStatusId(),
                sortState = getApiSortState()
            )
        }
    }
}