package com.xecoding.portfolio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xecoding.portfolio.common.NetworkResponse
import com.xecoding.portfolio.domain.use_case.GetAccountDetailsUseCase
import com.xecoding.portfolio.domain.use_case.GetAccountListUseCase
import com.xecoding.portfolio.ui.account_details.AccountDetailsState
import com.xecoding.portfolio.ui.account_list.AccountListState
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel: ViewModel(), KoinComponent {

    private val getAccountListUseCase: GetAccountListUseCase by inject()
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase by inject()

    // Keep state for Portfolio Screen
    private val _accounts = MutableStateFlow(AccountListState())
    val accounts: StateFlow<AccountListState> = _accounts

    // Keep state for Account info on Details Screen
    private val _accountDetails = MutableStateFlow(AccountDetailsState())
    val accountDetails: StateFlow<AccountDetailsState> = _accountDetails

    // Keep the selected id of the account that user clicked to navigate to
    private val _selectedAccountId: MutableStateFlow<String?> = MutableStateFlow(null)

    // Get the selected account from the current accounts
    val selectedAccount = _accounts.combine(_selectedAccountId) { list: AccountListState, id: String? ->
        list.accounts.filter { it.id == id }
    }

    init {
        getAccountList()
    }

    private fun getAccountList() {
        getAccountListUseCase().onEach {
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

    fun getAccountDetails(accountId: String) {
        getAccountDetailsUseCase(accountId).onEach {
            when (it) {
                is NetworkResponse.Loading ->
                    _accountDetails.value = AccountDetailsState(isLoading = true)
                is NetworkResponse.Success ->
                    _accountDetails.value = AccountDetailsState(accountDetails = it.data)
                is NetworkResponse.Error ->
                    _accountDetails.value = AccountDetailsState(error = it.message ?: "Unexpected error occurred")
            }
        }.launchIn(viewModelScope)
    }

    fun setSelectedAccountId(accountId: String?) {
        _selectedAccountId.value = accountId
    }

}