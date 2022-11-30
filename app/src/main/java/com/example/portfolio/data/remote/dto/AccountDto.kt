package com.example.portfolio.data.remote.dto

data class AccountDto(
    val account_nickname: String,
    val account_number: Int,
    val account_type: String,
    val balance: String,
    val currency_code: String,
    val id: String
)