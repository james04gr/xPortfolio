package com.xecoding.portfolio.domain.repository.accounts

import com.xecoding.portfolio.data.persistent.Account
import com.xecoding.portfolio.data.persistent.AppDatabase
import com.xecoding.portfolio.data.persistent.Details
import com.xecoding.portfolio.data.remote.api.PortfolioApi
import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.data.remote.dto.AccountDto
import com.xecoding.portfolio.data.remote.dto.TransactionsDto
import com.xecoding.portfolio.data.remote.dto.TransactionsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
    private val portfolioApi: PortfolioApi,
    private val appDatabase: AppDatabase
) : AccountRepository {

    override suspend fun fetchAccounts(): List<AccountDto> = portfolioApi.getAccounts()

    override suspend fun getAccountDetails(accountId: String): AccountDetailsDto =
        portfolioApi.getAccountDetails(accountId)

    override suspend fun getTransactions(
        accountId: String,
        transactionsRequest: TransactionsRequest
    ): TransactionsDto =
        portfolioApi.getTransactions(accountId, transactionsRequest)

    override fun getAccounts(): Flow<List<Account>> =
        appDatabase.accountsDao().getAccounts()

    override fun getAccount(accountId: String): Flow<Account?> =
        appDatabase.accountsDao().getAccount(accountId)

    override suspend fun getDatabaseAccounts(): List<Account> =
        appDatabase.accountsDao().getDatabaseAccounts()

    override suspend fun insertAccounts(accounts: List<Account>) = withContext(Dispatchers.IO) {
        appDatabase.accountsDao().insert(accounts)
    }

    override suspend fun updateFavorite(accountId: String?) {
        accountId?.let {
            appDatabase.accountsDao().resetFavorite()
            appDatabase.accountsDao().setFavorite(it)
        } ?:
            appDatabase.accountsDao().resetFavorite()
    }

    override suspend fun getDetails(accountId: String): Details? =
        appDatabase.detailsDao().getDetails(accountId)

    override suspend fun refreshDetails(details: Details) {
        appDatabase.detailsDao().refreshDetails(details)
    }

    override suspend fun deleteDetails() =
        appDatabase.detailsDao().delete()

}