package com.example.portfolio.data.remote.api

import com.example.portfolio.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestBuilder = request.newBuilder()

        requestBuilder.addHeader("username", BuildConfig.API_USERNAME)
        requestBuilder.addHeader("password", BuildConfig.API_PASSWORD)
        requestBuilder.addHeader("Server", "ktor-server-core/1.3.2 ktor-server-core/1.3.2")
        requestBuilder.addHeader("Content-Type", "application/json;charset=utf-8")
        requestBuilder.addHeader("Connection", "keep-alive")
        request = requestBuilder.build()

        return chain.proceed(request)
    }
}