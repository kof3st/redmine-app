package me.kofesst.android.redminecomposeapp.feature.presentation.project.list

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.project.Project
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.presentation.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModelBase() {
    private val _projects = MutableStateFlow<List<Project>>(listOf())
    val projects get() = _projects.asStateFlow()

    fun refreshData() {
        startLoading() {
            _projects.value = useCases.getProjects()
        }
    }
}