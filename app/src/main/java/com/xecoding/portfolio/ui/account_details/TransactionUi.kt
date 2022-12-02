package com.xecoding.portfolio.ui.account_details

import com.xecoding.portfolio.data.remote.dto.Transaction

sealed class TransactionUi {
    data class TransactionItem(val transaction: Transaction): TransactionUi()
    data class DateItem(val newDate: String): TransactionUi()
}
