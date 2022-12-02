package com.xecoding.portfolio.ui.account_details

import com.xecoding.portfolio.data.remote.dto.Transaction

data class AccountTransactionsState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val error: String? = null
)
