package com.example.portfolio.domain.repository

import com.example.portfolio.data.remote.api.PortfolioApi
import com.example.portfolio.data.remote.dto.AccountDto

class AccountRepositoryImpl(
    private val portfolioApi: PortfolioApi
) : AccountRepository {

    override suspend fun getAccounts(): List<AccountDto> = portfolioApi.getAccounts()
}