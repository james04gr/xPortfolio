package com.xecoding.portfolio.data.remote.api

import com.xecoding.portfolio.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// ToDo Headers for API
interface PortfolioApi {

    @GET("/accounts")
    suspend fun getAccounts(): List<AccountDto>

    @GET("/account/details/{account_id}")
    suspend fun getAccountDetails(
        @Path(value = "account_id") accountId: String
    ): AccountDetailsDto

    @POST("/account/transactions/{account_id}")
    suspend fun getTransactions(
        @Path(value = "account_id") accountId: String,
        @Body transactionRequest: TransactionsRequest
    ): TransactionsDto

    @POST("/account/transactions/{account_id}")
    suspend fun getFilteredTransactions(
        @Path(value = "account_id") accountId: String,
        @Body filteredTransactionsRequest: FilteredTransactionsRequest
    ): TransactionsDto

}