package com.xecoding.portfolio.domain.use_case

import com.xecoding.portfolio.common.NetworkResponse
import com.xecoding.portfolio.data.remote.dto.FilteredTransactionsRequest
import com.xecoding.portfolio.data.remote.dto.TransactionsDto
import com.xecoding.portfolio.data.remote.dto.TransactionsRequest
import com.xecoding.portfolio.domain.repository.accounts.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAccountTransactionsUseCase(
    private val accountRepository: AccountRepository
) {

    operator fun invoke(accountId: String, fromDate: String, toDate: String): Flow<NetworkResponse<TransactionsDto>> = flow {
        try {
            emit(NetworkResponse.Loading())
            val transactions = accountRepository.getTransactions(accountId, TransactionsRequest(next_page = 0))
            emit(NetworkResponse.Success(transactions))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResponse.Error(message = e.message ?: "An unexpected error occurred"))
        }
    }

}