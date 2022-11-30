package com.example.portfolio

import android.app.Application
import com.example.portfolio.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PortfolioApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PortfolioApplication)
            modules(
                arrayListOf(
                    networkModule
                )
            )
        }
    }

}