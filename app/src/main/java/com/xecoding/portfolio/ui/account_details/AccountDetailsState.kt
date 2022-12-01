package com.xecoding.portfolio.ui.account_details

import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto

data class AccountDetailsState(
    val isLoading: Boolean = false,
    val accountDetails: AccountDetailsDto? = null,
    val error: String? = null
)