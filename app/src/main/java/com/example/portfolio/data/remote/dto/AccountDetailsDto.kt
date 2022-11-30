package com.example.portfolio.data.remote.dto

data class AccountDetailsDto(
    val beneficiaries: List<String>,
    val branch: String,
    val opened_date: String,
    val product_name: String
)