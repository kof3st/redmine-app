package me.kofesst.android.redminecomposeapp.feature.presentation.project.item

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.presentation.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModelBase() {
    private val _issues = MutableStateFlow<List<Issue>>(listOf())
    val issues get() = _issues.asStateFlow()

    fun refreshData(projectId: Int) {
        startLoading {
            _issues.value = useCases.getIssues().filter { issue ->
                issue.project.id == projectId
            }
        }
    }
}