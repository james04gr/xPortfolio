package com.xecoding.portfolio.domain.repository.accounts

import com.xecoding.portfolio.data.persistent.Account
import com.xecoding.portfolio.data.persistent.Details
import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.data.remote.dto.AccountDto
import com.xecoding.portfolio.data.remote.dto.TransactionsDto
import com.xecoding.portfolio.data.remote.dto.TransactionsRequest
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun fetchAccounts(): List<AccountDto>

    suspend fun getAccountDetails(accountId: String): AccountDetailsDto

    suspend fun getTransactions(accountId: String, transactionsRequest: TransactionsRequest): TransactionsDto

    fun getAccounts(): Flow<List<Account>>

    fun getAccount(accountId: String): Flow<Account?>

    suspend fun getDatabaseAccounts(): List<Account>

    suspend fun insertAccounts(accounts: List<Account>)

    suspend fun updateFavorite(accountId: String?)

    suspend fun getDetails(accountId: String): Details?

    suspend fun refreshDetails(details: Details)

    suspend fun deleteDetails()

}