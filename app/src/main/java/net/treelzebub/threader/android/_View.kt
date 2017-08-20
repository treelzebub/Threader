package net.treelzebub.threader.android

import android.view.View
import android.view.ViewTreeObserver

/**
 * Created by Tre Murillo on 8/19/2017
 */

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

fun View.setVisibleInvisible(fn: () -> Boolean) = setVisibleInvisible(fn())
fun View.setVisibleInvisible(bool: Boolean) {
    visibility = if (bool) View.VISIBLE else View.INVISIBLE
}

fun View.setVisibleGone(fn: () -> Boolean) = setVisibleGone(fn())
fun View.setVisibleGone(bool: Boolean) {
    visibility = if (bool) View.VISIBLE else View.GONE
}

fun View.onNextLayout(fn: () -> Unit) {
    viewTreeObserver!!.addOnGlobalLayoutListener(SingleLayoutListener(this, fn))
}

private class SingleLayoutListener(
        private val view: View,
        private val fn: () -> Unit
) : ViewTreeObserver.OnGlobalLayoutListener {

    override fun onGlobalLayout() {
        // This line is why we need a subclass, and can't use the lambda syntax.
        view.viewTreeObserver!!.removeOnGlobalLayoutListener(this)
        fn()
    }
}