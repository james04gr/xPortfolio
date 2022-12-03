package com.xecoding.portfolio.domain.use_case

import com.xecoding.portfolio.domain.repository.accounts.AccountRepository

class DeleteFavoriteAccountUseCase(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke() {
        accountRepository.updateFavorite(null)
        accountRepository.deleteDetails()
    }

}