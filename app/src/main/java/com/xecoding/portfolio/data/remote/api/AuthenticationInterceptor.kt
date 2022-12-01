package com.xecoding.portfolio.data.remote.api

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor(
    private val username: String, private val password: String
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val credentials: String = Credentials.basic(username, password)
        var request = chain.request()
        val requestBuilder = request.newBuilder().apply {
            header("Authorization", credentials)
        }

        request = requestBuilder.build()

        return chain.proceed(request)
    }
}
