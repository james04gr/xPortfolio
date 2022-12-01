package com.xecoding.portfolio.domain.repository

import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.data.remote.dto.AccountDto

interface AccountRepository {

    suspend fun getAccounts(): List<AccountDto>

    suspend fun getAccountDetails(accountId: String): AccountDetailsDto

}