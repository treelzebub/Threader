package net.treelzebub.threader.android

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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