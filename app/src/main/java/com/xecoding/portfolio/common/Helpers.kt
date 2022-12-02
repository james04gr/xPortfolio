package com.xecoding.portfolio.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Spanned
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.xecoding.portfolio.common.Params.DEFAULT_DATE_FORMAT
import com.xecoding.portfolio.common.Params.FORMAT_dd_MM_yyyy
import com.xecoding.portfolio.data.remote.dto.AccountDto
import java.text.SimpleDateFormat
import java.util.*

fun runOnUiThread(code: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        run(code)
    }
}

fun AppCompatActivity.toast(text: String, length: Int = Toast.LENGTH_SHORT) {
    runOnUiThread { Toast.makeText(this, text, length).show() }
}

fun Fragment.toast(text: String) {
    runOnUiThread { Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show() }
}

fun Context.toast(text: String, length: Int = Toast.LENGTH_LONG) {
    runOnUiThread { Toast.makeText(this, text, length).show() }
}

fun String.asHtml(): Spanned =
    HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)

fun Long.formatAs(format: String = DEFAULT_DATE_FORMAT): String =
    SimpleDateFormat(format).format(Date(this))

@SuppressLint("SimpleDateFormat")
fun fromStringToDate(stringDate: String?): Date? {
    if (stringDate.isNullOrEmpty()) return null
    val formatter = SimpleDateFormat(DEFAULT_DATE_FORMAT)
    return formatter.parse(stringDate) as Date
}

fun isSameDay(date1: String, date2: String): Boolean =
    SimpleDateFormat(FORMAT_dd_MM_yyyy).format(fromStringToDate(date1)) == SimpleDateFormat(FORMAT_dd_MM_yyyy).format(
        fromStringToDate(date2)
    )

fun AccountDto.displayName(): String =
    if (TextUtils.isEmpty(account_nickname)) account_number.toString()
    else account_nickname

fun AccountDto.amountWithCurrency(): String = "$balance $currency_code"