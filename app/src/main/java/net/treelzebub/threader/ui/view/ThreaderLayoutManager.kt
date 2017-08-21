package net.treelzebub.threader.ui.view

import android.content.Context
import android.graphics.PointF
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.Log
import net.treelzebub.threader.runtime.TAG

/**
 * Created by Tre Murillo on 8/19/2017
 */
class ThreaderLayoutManager @JvmOverloads constructor(
        context: Context,
        orientation: Int = VERTICAL,
        reverseLayout: Boolean = false
): LinearLayoutManager(context, orientation, reverseLayout) {

    override fun onItemsAdded(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        val child = recyclerView.getChildAt(childCount - 1)
        Log.d(TAG, "added to position ${recyclerView.getChildAdapterPosition(child)}")
        child.requestFocus()
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView,
                                        state: RecyclerView.State, position: Int) {
        val smoothScroller = TopSnappedSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private inner class TopSnappedSmoothScroller(c: Context) : LinearSmoothScroller(c) {
        val anchor = this@ThreaderLayoutManager
        override fun computeScrollVectorForPosition(targetPosition: Int): PointF {
            return anchor.computeScrollVectorForPosition(targetPosition)
        }

        override fun getVerticalSnapPreference() = SNAP_TO_START
    }
}