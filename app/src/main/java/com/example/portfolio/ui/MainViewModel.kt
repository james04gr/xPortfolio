package com.example.portfolio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolio.common.NetworkResponse
import com.example.portfolio.domain.use_case.GetAccountsUseCase
import com.example.portfolio.ui.account_list.AccountListState
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel: ViewModel(), KoinComponent {

    private val getAccountsUseCase: GetAccountsUseCase by inject()

    private val _accounts = MutableStateFlow(AccountListState())
    val accounts: StateFlow<AccountListState> = _accounts

    init {
        getAccounts()
    }

    private fun getAccounts() {
        getAccountsUseCase().onEach {
            when (it) {
                is NetworkResponse.Loading ->
                    _accounts.value = AccountListState(isLoading = true)
                is NetworkResponse.Success ->
                    _accounts.value = AccountListState(accounts = it.data ?: emptyList())
                is NetworkResponse.Error ->
                    _accounts.value = AccountListState(error = it.message ?: "Unexpected error occurred")
            }
        }.launchIn(viewModelScope)
    }

}