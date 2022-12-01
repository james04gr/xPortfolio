package com.xecoding.portfolio.ui.account_list

import com.xecoding.portfolio.data.remote.dto.AccountDto

interface AccountListItemClicked {

    fun onAccountClicked(accountDto: AccountDto)

}