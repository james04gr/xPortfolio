package com.xecoding.portfolio.domain.use_case

import com.xecoding.portfolio.common.NetworkResponse
import com.xecoding.portfolio.domain.repository.accounts.AccountRepository
import com.xecoding.portfolio.common.refreshAccounts
import com.xecoding.portfolio.common.toFormalString
import com.xecoding.portfolio.data.persistent.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAccountListUseCase(
    private val accountRepository: AccountRepository
) {

    operator fun invoke(): Flow<NetworkResponse<List<Account>>> = flow {
        try {
            emit(NetworkResponse.Loading())
            val accounts = accountRepository.fetchAccounts()
            val databaseAccounts = accountRepository.getDatabaseAccounts()
            val newAccounts = refreshAccounts(accounts, databaseAccounts)
            accountRepository.insertAccounts(newAccounts)
            emit(NetworkResponse.Success(data = newAccounts))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResponse.Error(
                message = e.toFormalString()
            ))
        }
    }

}