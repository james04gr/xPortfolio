package com.xecoding.portfolio.ui.account_list

import com.xecoding.portfolio.data.persistent.Account

data class AccountListState(
    val isLoading: Boolean = false,
    val accounts: List<Account> = emptyList(),
    val error: String? = null
)