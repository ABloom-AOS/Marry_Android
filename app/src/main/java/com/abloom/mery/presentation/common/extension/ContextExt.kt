package com.abloom.mery.presentation.common.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

private var toast: Toast? = null

fun Context.showToast(text: String) {
    if (toast != null) toast!!.cancel()
    toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
    toast!!.show()
}

fun Context.showToast(@StringRes textResId: Int) {
    showToast(getString(textResId))
}

fun Context.copyToClipboard(label: String, text: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clipData)
}
