package me.kofesst.android.redminecomposeapp.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.domain.util.UserHolder
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: UseCases,
    val userHolder: UserHolder,
) : ViewModelBase() {

    fun clearSession(onCleared: () -> Unit) {
        startLoading(
            onSuccessCallback = onCleared
        ) {
            useCases.clearSession()
            userHolder.currentUser.value = null
        }
    }
}