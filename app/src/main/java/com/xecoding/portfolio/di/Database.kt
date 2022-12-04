package com.xecoding.portfolio.di

import androidx.room.Room
import com.xecoding.portfolio.BuildConfig
import com.xecoding.portfolio.data.persistent.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        return@single BuildConfig.DATABASE_NAME
    }

    single {
        return@single Room
            .databaseBuilder(androidContext(), AppDatabase::class.java, get())
            .build()
    }
}