package com.example.portfolio.di

import com.example.portfolio.domain.repository.AccountRepository
import com.example.portfolio.domain.repository.AccountRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<AccountRepository> {
        return@single AccountRepositoryImpl(get())
    }

}