package com.xecoding.portfolio.data.remote.dto

data class TransactionsDto(
    val paging: Paging,
    val transactions: List<Transaction>
)