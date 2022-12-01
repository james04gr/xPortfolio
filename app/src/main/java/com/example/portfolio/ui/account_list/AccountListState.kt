package com.example.portfolio.ui.account_list

import com.example.portfolio.data.remote.dto.AccountDto

data class AccountListState(
    val isLoading: Boolean = false,
    val accounts: List<AccountDto> = emptyList(),
    val error: String? = null
)