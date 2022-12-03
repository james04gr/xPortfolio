package com.xecoding.portfolio

import android.app.Application
import com.xecoding.portfolio.di.databaseModule
import com.xecoding.portfolio.di.networkModule
import com.xecoding.portfolio.di.repositoryModule
import com.xecoding.portfolio.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PortfolioApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PortfolioApplication)
            modules(
                arrayListOf(
                    networkModule, repositoryModule, useCaseModule, databaseModule
                )
            )
        }
    }

}