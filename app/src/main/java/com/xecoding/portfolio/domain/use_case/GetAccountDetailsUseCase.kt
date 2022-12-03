package com.xecoding.portfolio.domain.use_case

import com.xecoding.portfolio.common.NetworkResponse
import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.domain.repository.accounts.AccountRepository
import com.google.gson.Gson
import com.xecoding.portfolio.common.toAccountDetailsDto
import com.xecoding.portfolio.common.toFormalString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAccountDetailsUseCase(
    private val accountRepository: AccountRepository
) {

    operator fun invoke(accountId: String): Flow<NetworkResponse<AccountDetailsDto>> = flow {
        try {
            emit(NetworkResponse.Loading())
            val accountDetails = accountRepository.getAccountDetails(accountId)
            emit(NetworkResponse.Success(accountDetails))
        } catch (e: Exception) {
            e.printStackTrace()
            val details = accountRepository.getDetails(accountId)
            emit(NetworkResponse.Error(
                data = details?.toAccountDetailsDto(),
                message = e.toFormalString()
            ))
        }
    }

}