package com.xecoding.portfolio.di

import com.xecoding.portfolio.domain.use_case.*
import org.koin.dsl.module

val useCaseModule = module {

    single<GetAccountListUseCase> {
        return@single GetAccountListUseCase(get())
    }

    single<GetAccountDetailsUseCase> {
        return@single GetAccountDetailsUseCase(get())
    }

    single<InsertFavoriteAccountUseCase> {
        return@single InsertFavoriteAccountUseCase(get())
    }

    single<DeleteFavoriteAccountUseCase> {
        return@single DeleteFavoriteAccountUseCase(get())
    }

    single<ObserveAccountsUseCase> {
        return@single ObserveAccountsUseCase(get())
    }

    single<GetAccountUseCase> {
        return@single GetAccountUseCase(get())
    }

}