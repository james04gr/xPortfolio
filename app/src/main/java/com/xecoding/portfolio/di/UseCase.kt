package com.xecoding.portfolio.di

import com.xecoding.portfolio.domain.use_case.GetAccountDetailsUseCase
import com.xecoding.portfolio.domain.use_case.GetAccountListUseCase
import org.koin.dsl.module

val useCaseModule = module {

    single<GetAccountListUseCase> {
        return@single GetAccountListUseCase(get())
    }

    single<GetAccountDetailsUseCase> {
        return@single GetAccountDetailsUseCase(get())
    }

}