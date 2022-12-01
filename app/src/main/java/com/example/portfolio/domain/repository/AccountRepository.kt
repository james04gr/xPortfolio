package com.example.portfolio.domain.repository

import com.example.portfolio.data.remote.dto.AccountDetailsDto
import com.example.portfolio.data.remote.dto.AccountDto

interface AccountRepository {

    suspend fun getAccounts(): List<AccountDto>

    suspend fun getAccountDetails(accountId: String): AccountDetailsDto

}