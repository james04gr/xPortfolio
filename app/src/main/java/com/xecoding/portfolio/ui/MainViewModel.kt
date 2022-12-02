package com.xecoding.portfolio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.xecoding.portfolio.common.NetworkResponse
import com.xecoding.portfolio.common.isSameDay
import com.xecoding.portfolio.data.remote.dto.Transaction
import com.xecoding.portfolio.domain.repository.transactions.TransactionsPagingSource
import com.xecoding.portfolio.domain.use_case.GetAccountDetailsUseCase
import com.xecoding.portfolio.domain.use_case.GetAccountListUseCase
import com.xecoding.portfolio.domain.use_case.GetAccountTransactionsUseCase
import com.xecoding.portfolio.ui.account_details.AccountDetailsState
import com.xecoding.portfolio.ui.account_details.AccountTransactionsState
import com.xecoding.portfolio.ui.account_details.InputSource
import com.xecoding.portfolio.ui.account_details.TransactionUi
import com.xecoding.portfolio.ui.account_list.AccountListState
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel: ViewModel(), KoinComponent {

    private val getAccountListUseCase: GetAccountListUseCase by inject()
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase by inject()
    private val getAccountTransactionsUseCase: GetAccountTransactionsUseCase by inject()

    // Keep state for Portfolio Screen
    private val _accounts = MutableStateFlow(AccountListState())
    val accounts: StateFlow<AccountListState> = _accounts

    // Keep state for Account info on Details Screen
    private val _accountDetails = MutableStateFlow(AccountDetailsState())
    val accountDetails: StateFlow<AccountDetailsState> = _accountDetails

    // Keep state for Account info on Details Screen
    private val _accountTransactions = MutableStateFlow(AccountTransactionsState())
    val accountTransactions: StateFlow<AccountTransactionsState> = _accountTransactions

    // Keep the selected id of the account that user clicked to navigate to
    private val _selectedAccountId: MutableStateFlow<String?> = MutableStateFlow(null)

    // Get the selected account from the current accounts
    val selectedAccount = _accounts.combine(_selectedAccountId) { list: AccountListState, id: String? ->
        list.accounts.filter { it.id == id }
    }

    private val _inputSource  = MutableStateFlow(InputSource())
    fun setInputSource(accountId: String, fromDate: String, toDate: String) {
        _inputSource.value = InputSource(accountId, fromDate, toDate)
    }

    private val _isFiltered = MutableStateFlow(false)
    val isFiltered = _isFiltered
    fun setIsFiltered(flag: Boolean) {
        _isFiltered.value = flag
    }

    // Here we switch out observe to trigger a new PagingSource
    // in order to distinguish between All transactions - Filtered transactions
    val transactionsFlow: Flow<PagingData<TransactionUi>> = _inputSource.flatMapLatest { input ->
        Pager(
            config = PagingConfig(pageSize = 10)
        ) {
            TransactionsPagingSource(
                accountId = input.accountId,
                fromDate = input.fromDate,
                toDate = input.toDate
            )
        }.flow.cachedIn(viewModelScope).map { pagingData ->
            pagingData.map { t ->
                TransactionUi.TransactionItem(t)
            }.insertSeparators { before: TransactionUi.TransactionItem?, after: TransactionUi.TransactionItem? ->
                when {
                    before == null && after == null -> null
                    before != null && after == null -> TransactionUi.DateItem(before.transaction.date)
                    before == null && after != null -> TransactionUi.DateItem(after.transaction.date)
                    before != null && after != null ->
                        if (!isSameDay(
                                before.transaction.date,
                                after.transaction.date
                            )
                        )
                            TransactionUi.DateItem(after.transaction.date)
                        else null
                    else -> null
                }
            }
        }
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

    fun getAccountTransactions(accountId: String, fromDate: String = "", toDate: String = "") {
        getAccountTransactionsUseCase(accountId, fromDate, toDate).onEach {
            when (it) {
                is NetworkResponse.Loading ->
                    _accountTransactions.value = AccountTransactionsState(isLoading = true)
                is NetworkResponse.Success ->
                    _accountTransactions.value = AccountTransactionsState(transactions = it.data?.transactions ?: emptyList())
                is NetworkResponse.Error ->
                    _accountTransactions.value = AccountTransactionsState(error = it.message ?: "Unexpected error occurred")
            }
        }.launchIn(viewModelScope)
    }

    fun setSelectedAccountId(accountId: String?) {
        _selectedAccountId.value = accountId
    }

}