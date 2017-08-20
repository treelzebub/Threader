package net.treelzebub.threader.android

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Created by Tre Murillo on 8/19/2017
 */

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.copyToClipboard(str: String) {
    val clipboardMgr = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardMgr.primaryClip = ClipData.newPlainText("Threader", str)
}

fun Activity.dismissKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
}