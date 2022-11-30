package com.example.portfolio.data.remote.api

import com.example.portfolio.common.NetworkResponse
import com.example.portfolio.data.remote.dto.AccountDetailsDto
import com.example.portfolio.data.remote.dto.AccountDto
import retrofit2.http.GET
import retrofit2.http.Path

// ToDo Headers for API
interface PortfolioApi {

    @GET("/accounts")
    suspend fun getAccounts(): NetworkResponse<List<AccountDto>>

    @GET("/account/details/{account_id}")
    suspend fun getAccountDetails(
        @Path(value = "account_id") accountId: String
    ): NetworkResponse<AccountDetailsDto>

}