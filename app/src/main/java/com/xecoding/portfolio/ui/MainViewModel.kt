package com.xecoding.portfolio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.xecoding.portfolio.common.NetworkResponse
import com.xecoding.portfolio.common.isSameDay
import com.xecoding.portfolio.common.toDetails
import com.xecoding.portfolio.data.persistent.Account
import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.domain.repository.transactions.TransactionsPagingSource
import com.xecoding.portfolio.domain.use_case.*
import com.xecoding.portfolio.ui.account_details.AccountDetailsState
import com.xecoding.portfolio.ui.account_details.InputSource
import com.xecoding.portfolio.ui.account_details.TransactionUi
import com.xecoding.portfolio.ui.account_list.AccountListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val getAccountListUseCase: GetAccountListUseCase by inject()
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase by inject()
    private val insertFavoriteAccountUseCase: InsertFavoriteAccountUseCase by inject()
    private val deleteFavoriteAccountUseCase: DeleteFavoriteAccountUseCase by inject()
    private val observeAccountsUseCase: ObserveAccountsUseCase by inject()
    private val getAccountUseCase: GetAccountUseCase by inject()

    // Keep state for Portfolio Screen
    private val _accountsState = MutableStateFlow(AccountListState())
    val accountsState: StateFlow<AccountListState> = _accountsState

    // Keep state for Account info on Details Screen
    private val _accountDetailsState = MutableStateFlow(AccountDetailsState())

    val accounts: Flow<List<Account>> = observeAccountsUseCase()

    // Keep state for Account info on Details Screen
//    private val _accountTransactions = MutableStateFlow(AccountTransactionsState())
//    val accountTransactions: StateFlow<AccountTransactionsState> = _accountTransactions

    // Keep the selected id of the account that user clicked to navigate to
    private val _selectedAccountId: MutableStateFlow<String> = MutableStateFlow("")

    // Get the selected account from the current accounts
    private val _selectedAccount = _selectedAccountId.flatMapLatest { selectedId ->
        getAccountUseCase(selectedId)
    }

    val accountWithDetails: Flow<Pair<Account, AccountDetailsState>?> = _selectedAccount.combine(_accountDetailsState) { currentAccount, detailsState ->
        if (currentAccount != null)
            Pair(currentAccount, detailsState)
        else null
    }

    private val _inputSource = MutableStateFlow(InputSource())
    fun setInputSource(accountId: String, fromDate: String, toDate: String) {
        _inputSource.value = InputSource(accountId, fromDate, toDate)
    }

    private val _isFiltered = MutableStateFlow(false)
    val isFiltered = _isFiltered
    fun setIsFiltered(flag: Boolean) {
        _isFiltered.value = flag
    }

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite
    fun setIsFavorite(flag: Boolean) {
        _isFavorite.value = flag
    }

    init {
        getAccountList()
    }

    fun refreshAccounts() {
        getAccountList()
    }

    private fun getAccountList() {
        getAccountListUseCase().onEach {
            when (it) {
                is NetworkResponse.Loading -> _accountsState.value =
                    AccountListState(isLoading = true)
                is NetworkResponse.Success -> _accountsState.value =
                    AccountListState(accounts = it.data ?: emptyList())
                is NetworkResponse.Error -> _accountsState.value =
                    AccountListState(error = it.message ?: "Unexpected error occurred")
            }
        }.launchIn(viewModelScope)
    }

    fun getAccountDetails(accountId: String) {
        getAccountDetailsUseCase(accountId).onEach {
            when (it) {
                is NetworkResponse.Loading -> _accountDetailsState.value =
                    AccountDetailsState(isLoading = true)
                is NetworkResponse.Success -> _accountDetailsState.value =
                    AccountDetailsState(accountDetails = it.data)
                is NetworkResponse.Error -> _accountDetailsState.value = AccountDetailsState(
                    accountDetails = it.data,
                    error = it.message ?: "Unexpected error occurred"
                )
            }
        }.launchIn(viewModelScope)
    }

    // Here we switch out observe to trigger a new PagingSource
    // in order to distinguish between All transactions - Filtered transactions
    val transactionsFlow: Flow<PagingData<TransactionUi>> = _inputSource.flatMapLatest { input ->
        Pager(
            config = PagingConfig(pageSize = 10)
        ) {
            TransactionsPagingSource(
                accountId = input.accountId, fromDate = input.fromDate, toDate = input.toDate
            )
        }.flow.cachedIn(viewModelScope).map { pagingData ->
            pagingData.map { t ->
                TransactionUi.TransactionItem(t)
            }
                .insertSeparators { before: TransactionUi.TransactionItem?, after: TransactionUi.TransactionItem? ->
                    when {
                        before == null && after == null -> null
                        before != null && after == null -> TransactionUi.DateItem(before.transaction.date)
                        before == null && after != null -> TransactionUi.DateItem(after.transaction.date)
                        before != null && after != null -> if (!isSameDay(
                                before.transaction.date, after.transaction.date
                            )
                        ) TransactionUi.DateItem(after.transaction.date)
                        else null
                        else -> null
                    }
                }
        }
    }

    /*fun getAccountTransactions(accountId: String, fromDate: String = "", toDate: String = "") {
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
    }*/

    fun setSelectedAccountId(accountId: String) {
        _selectedAccountId.value = accountId
    }

    fun saveFavorite(account: Account, accountDetailsDto: AccountDetailsDto?) {
        viewModelScope.launch(Dispatchers.IO) {
            accountDetailsDto?.let { ad ->
                setIsFavorite(true)
                insertFavoriteAccountUseCase.invoke(account.id, ad.toDetails(account.id))
            }
        }
    }

    fun deleteFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            setIsFavorite(false)
            deleteFavoriteAccountUseCase.invoke()
        }
    }

}