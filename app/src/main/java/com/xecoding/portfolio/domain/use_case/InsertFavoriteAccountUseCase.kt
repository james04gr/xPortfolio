package com.xecoding.portfolio.domain.use_case

import com.xecoding.portfolio.data.persistent.Details
import com.xecoding.portfolio.domain.repository.accounts.AccountRepository

class InsertFavoriteAccountUseCase(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(accountId: String, details: Details) {
        accountRepository.updateFavorite(accountId)
        accountRepository.refreshDetails(details)
    }

}