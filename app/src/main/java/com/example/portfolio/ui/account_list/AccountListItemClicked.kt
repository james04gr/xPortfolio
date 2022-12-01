package com.example.portfolio.ui.account_list

import com.example.portfolio.data.remote.dto.AccountDto

interface AccountListItemClicked {

    fun onAccountClicked(accountDto: AccountDto)

}