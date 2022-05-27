package me.kofesst.android.redminecomposeapp.presentation.accounts.list

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.domain.model.Account
import me.kofesst.android.redminecomposeapp.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.presentation.ViewModelBase
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModelBase() {
    private val _accounts = MutableStateFlow<List<Account>>(listOf())
    val accounts get() = _accounts.asStateFlow()

    fun refreshData() {
        startLoading {
            _accounts.value = useCases.getAccounts()
        }
    }
}