package me.kofesst.android.redminecomposeapp.feature.presentation.project.item

import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.presentation.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModelBase() {
//    private val _issues = mutableStateOf<List<Issue()
}