package com.xecoding.portfolio.di

import com.xecoding.portfolio.domain.repository.AccountRepository
import com.xecoding.portfolio.domain.repository.AccountRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<AccountRepository> {
        return@single AccountRepositoryImpl(get())
    }

}