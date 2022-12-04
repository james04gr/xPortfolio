package com.xecoding.portfolio.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.xecoding.portfolio.data.remote.api.PortfolioApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val TestNetworkModule = module {

    single<OkHttpClient> {
        return@single OkHttpClient().newBuilder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit> {
        return@single Retrofit.Builder()
            .baseUrl(get<MockWebServer>().url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(get<OkHttpClient>())
            .build()
    }

    single<MockWebServer> {
        return@single MockWebServer()
    }

    single<PortfolioApi> {
        val retrofit = get<Retrofit>()
        return@single retrofit.create(PortfolioApi::class.java)
    }

}