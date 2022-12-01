package com.xecoding.portfolio.domain.repository

import com.xecoding.portfolio.data.remote.api.PortfolioApi
import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.data.remote.dto.AccountDto

class AccountRepositoryImpl(
    private val portfolioApi: PortfolioApi
) : AccountRepository {

    override suspend fun getAccounts(): List<AccountDto> = portfolioApi.getAccounts()

    override suspend fun getAccountDetails(accountId: String): AccountDetailsDto =
        portfolioApi.getAccountDetails(accountId)
}