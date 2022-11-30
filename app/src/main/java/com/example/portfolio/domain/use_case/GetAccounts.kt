package com.example.portfolio.domain.use_case

import com.example.portfolio.common.NetworkResponse
import com.example.portfolio.data.remote.dto.AccountDto
import com.example.portfolio.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// TODO DI for Usecase
class GetAccounts(
    private val accountRepository: AccountRepository
) {

    operator fun invoke(): Flow<NetworkResponse<List<AccountDto>>> = flow {
        try {
            emit(NetworkResponse.Loading())
            val allAccounts = accountRepository.getAccounts()
            emit(NetworkResponse.Success(allAccounts))
        } catch (e: Exception) {
            emit(NetworkResponse.Error(message = e.message ?: "An unexpected error occurred"))
        }
    }

}