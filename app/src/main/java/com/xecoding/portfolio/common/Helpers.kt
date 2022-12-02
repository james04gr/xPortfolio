package com.xecoding.portfolio.common

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Spanned
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
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

fun Long.formatAs(format: String = Params.DEFAULT_DATE_FORMAT): String =
    SimpleDateFormat(format).format(Date(this))