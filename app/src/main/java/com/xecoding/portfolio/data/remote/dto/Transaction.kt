package com.xecoding.portfolio.data.remote.dto

data class Transaction(
    val date: String,
    val description: String?,
    val id: String,
    val is_debit: Boolean,
    val transaction_amount: String,
    val transaction_type: String
)