package com.example.portfolio.di

import com.example.portfolio.domain.use_case.GetAccountsUseCase
import org.koin.dsl.module

val useCaseModule = module {

    single<GetAccountsUseCase> {
        return@single GetAccountsUseCase(get())
    }

}