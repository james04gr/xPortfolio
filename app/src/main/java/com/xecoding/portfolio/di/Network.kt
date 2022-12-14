package com.xecoding.portfolio.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.xecoding.portfolio.BuildConfig
import com.xecoding.portfolio.data.remote.ConnectivityObserver
import com.xecoding.portfolio.data.remote.api.AuthenticationInterceptor
import com.xecoding.portfolio.data.remote.api.HeaderInterceptor
import com.xecoding.portfolio.data.remote.api.PortfolioApi
import com.xecoding.portfolio.data.remote.ConnectivityObserverImpl
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import java.util.concurrent.TimeUnit

val networkModule = module {

    single<HttpLoggingInterceptor> {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return@single httpLoggingInterceptor
    }

    single<HeaderInterceptor> {
        return@single HeaderInterceptor()
    }

    single<AuthenticationInterceptor> {
        return@single AuthenticationInterceptor(
            BuildConfig.API_USERNAME, BuildConfig.API_PASSWORD
        )
    }

    single<OkHttpClient> {
        val httpClientBuilder = OkHttpClient().newBuilder().apply {
            connectTimeout(20, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
            addInterceptor(get<HeaderInterceptor>())
            addInterceptor(get<AuthenticationInterceptor>())
            addInterceptor(get<HttpLoggingInterceptor>())
        }

        return@single httpClientBuilder.build()
    }

    single<PortfolioApi> {
        return@single Retrofit.Builder().apply {
            baseUrl(BuildConfig.BASE_URL)
            addCallAdapterFactory(CoroutineCallAdapterFactory())
            addConverterFactory(GsonConverterFactory.create())
            client(get<OkHttpClient>())
        }.build().create(PortfolioApi::class.java)
    }

    single<ConnectivityObserver> {
        return@single ConnectivityObserverImpl(androidContext())
    }
}