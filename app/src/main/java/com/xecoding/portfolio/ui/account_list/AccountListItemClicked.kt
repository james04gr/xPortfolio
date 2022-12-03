package com.xecoding.portfolio.ui.account_list

import com.xecoding.portfolio.data.persistent.Account

interface AccountListItemClicked {

    fun onAccountClicked(account: Account)

}