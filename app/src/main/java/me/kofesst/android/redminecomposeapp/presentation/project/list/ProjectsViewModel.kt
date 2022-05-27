package me.kofesst.android.redminecomposeapp.presentation.project.list

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.domain.model.Project
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.presentation.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModelBase() {
    private val _projects = MutableStateFlow<List<Project>>(listOf())
    val projects get() = _projects.asStateFlow()

    fun refreshData() {
        startLoading() {
            _projects.value = useCases.getProjects()
        }
    }
}