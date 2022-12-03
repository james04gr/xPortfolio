package com.xecoding.portfolio.domain.use_case

import com.xecoding.portfolio.data.persistent.Account
import com.xecoding.portfolio.domain.repository.accounts.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObserveAccountsUseCase(
    private val accountRepository: AccountRepository
) {

    operator fun invoke(): Flow<List<Account>> = accountRepository.getAccounts()

}