package com.xecoding.portfolio.domain.use_case

import com.xecoding.portfolio.data.persistent.Account
import com.xecoding.portfolio.domain.repository.accounts.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetAccountUseCase(
    private val accountRepository: AccountRepository
) {

    operator fun invoke(accountId: String): Flow<Account?> = accountRepository.getAccount(accountId)

}