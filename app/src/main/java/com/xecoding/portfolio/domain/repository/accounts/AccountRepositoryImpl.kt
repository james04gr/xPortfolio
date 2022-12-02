package com.xecoding.portfolio.domain.repository.accounts

import com.xecoding.portfolio.data.remote.api.PortfolioApi
import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.data.remote.dto.AccountDto
import com.xecoding.portfolio.data.remote.dto.TransactionsDto
import com.xecoding.portfolio.data.remote.dto.TransactionsRequest

class AccountRepositoryImpl(
    private val portfolioApi: PortfolioApi
) : AccountRepository {

    override suspend fun getAccounts(): List<AccountDto> = portfolioApi.getAccounts()

    override suspend fun getAccountDetails(accountId: String): AccountDetailsDto =
        portfolioApi.getAccountDetails(accountId)

    override suspend fun getTransactions(accountId: String, transactionsRequest: TransactionsRequest): TransactionsDto =
        portfolioApi.getTransactions(accountId, transactionsRequest)
}