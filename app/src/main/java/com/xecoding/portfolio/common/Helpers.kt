package com.xecoding.portfolio.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Spanned
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.xecoding.portfolio.common.Params.DATE_SEPARATOR_FORMAT
import com.xecoding.portfolio.common.Params.DEFAULT_DATE_FORMAT
import com.xecoding.portfolio.common.Params.FORMAT_dd_MM_yyyy
import com.xecoding.portfolio.data.persistent.Account
import com.xecoding.portfolio.data.persistent.Details
import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.data.remote.dto.AccountDto
import java.text.SimpleDateFormat
import java.util.*

fun runOnUiThread(code: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        run(code)
    }
}

fun Context.toast(text: String, length: Int = Toast.LENGTH_SHORT) {
    runOnUiThread { Toast.makeText(this, text, length).show() }
}

fun String.asHtml(): Spanned = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)

fun Long.formatAs(format: String = DEFAULT_DATE_FORMAT): String =
    SimpleDateFormat(format).format(Date(this))

fun Int.toBoolean() = this == 1

@SuppressLint("SimpleDateFormat")
fun fromStringToDate(stringDate: String?): Date? {
    if (stringDate.isNullOrEmpty()) return null
    val formatter = SimpleDateFormat(DEFAULT_DATE_FORMAT)
    return formatter.parse(stringDate) as Date
}

fun isSameDay(date1: String, date2: String): Boolean =
    SimpleDateFormat(FORMAT_dd_MM_yyyy).format(fromStringToDate(date1)) == SimpleDateFormat(
        FORMAT_dd_MM_yyyy
    ).format(
        fromStringToDate(date2)
    )

fun String.formatAs(format: String = DATE_SEPARATOR_FORMAT): String {
    val formatter = SimpleDateFormat(DEFAULT_DATE_FORMAT)
    val newDate = formatter.parse(this) ?: return ""
    return SimpleDateFormat(format).format(newDate)
}

fun Account.displayName(): String =
    if (account_nickname.isNullOrEmpty()) account_number.toString()
    else account_nickname

fun Account.amountWithCurrency(): String = "$balance $currency_code"

fun refreshAccounts(accountDtos: List<AccountDto>, accounts: List<Account>): List<Account> {
    val result = mutableListOf<Account>()
    accountDtos.forEach {
        result.add(
            Account(
                id = it.id,
                account_nickname = it.account_nickname,
                account_number = it.account_number,
                account_type = it.account_type,
                balance = it.balance,
                currency_code = it.currency_code,
                isFavorite = accounts.find { a -> a.id == it.id }?.isFavorite ?: 0
            )
        )
    }

    return result
}

fun AccountDetailsDto.toDetails(accountId: String): Details =
    Details(
        id = accountId,
        beneficiaries = beneficiaries,
        branch = branch,
        opened_date = opened_date,
        product_name = product_name
    )

fun Details.toAccountDetailsDto(): AccountDetailsDto =
    AccountDetailsDto(
        beneficiaries = beneficiaries,
        branch = branch,
        opened_date = opened_date,
        product_name = product_name
    )

fun Exception.toFormalString(): String =
    when (this) {
        is java.net.UnknownHostException -> "Connectivity error"
        is retrofit2.HttpException -> "Network request error occurred"
        else -> this.message ?: "Unexpected error"
    }