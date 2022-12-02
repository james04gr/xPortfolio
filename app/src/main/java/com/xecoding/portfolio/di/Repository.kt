package com.xecoding.portfolio.di

import com.xecoding.portfolio.domain.repository.accounts.AccountRepository
import com.xecoding.portfolio.domain.repository.accounts.AccountRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<AccountRepository> {
        return@single AccountRepositoryImpl(get())
    }

}