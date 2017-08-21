package net.treelzebub.threader.android

import android.app.Activity
import android.view.View

/**
 * Created by Tre Murillo on 8/19/2017
 */

fun Activity.onNextLayout(fn: () -> Unit) {
    findViewById<View>(android.R.id.content).onNextLayout(fn)
}