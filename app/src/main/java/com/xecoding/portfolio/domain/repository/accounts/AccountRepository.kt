package com.xecoding.portfolio.domain.repository.accounts

import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.data.remote.dto.AccountDto
import com.xecoding.portfolio.data.remote.dto.TransactionsDto
import com.xecoding.portfolio.data.remote.dto.TransactionsRequest

interface AccountRepository {

    suspend fun getAccounts(): List<AccountDto>

    suspend fun getAccountDetails(accountId: String): AccountDetailsDto

    suspend fun getTransactions(accountId: String, transactionsRequest: TransactionsRequest): TransactionsDto

}