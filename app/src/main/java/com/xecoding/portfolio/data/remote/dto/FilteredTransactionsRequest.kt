package com.xecoding.portfolio.data.remote.dto

data class FilteredTransactionsRequest(
    val from_date: String,
    val next_page: Int,
    val to_date: String
)