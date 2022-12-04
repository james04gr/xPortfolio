package com.xecoding.portfolio.di

import com.xecoding.portfolio.Utils
import com.xecoding.portfolio.Utils.ACCOUNT_DETAILS
import com.xecoding.portfolio.Utils.ACCOUNT_LIST
import com.xecoding.portfolio.Utils.TRANSACTIONS
import okhttp3.mockwebserver.MockResponse
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.net.HttpURLConnection

val ApiResponseModule = module {
    single(named(ACCOUNT_LIST)) {
        return@single Utils.readTestResourceFile("${ACCOUNT_LIST.lowercase()}.json")
    }

    single(named(ACCOUNT_DETAILS)) {
        return@single Utils.readTestResourceFile("${ACCOUNT_DETAILS.lowercase()}.json")
    }

    single(named(TRANSACTIONS)) {
        return@single Utils.readTestResourceFile("${TRANSACTIONS.lowercase()}.json")
    }

    single(named(ACCOUNT_LIST)) {
        return@single MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(get<String>(named(ACCOUNT_LIST)))
    }

    single(named(ACCOUNT_DETAILS)) {
        return@single MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(get<String>(named(ACCOUNT_DETAILS)))
    }

    single(named(TRANSACTIONS)) {
        return@single MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(get<String>(named(TRANSACTIONS)))
    }
}
