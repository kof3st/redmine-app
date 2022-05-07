package me.kofesst.android.redminecomposeapp.feature.presentation.accounts.list

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.kofesst.android.redminecomposeapp.feature.data.model.account.Account
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.presentation.ViewModelBase
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