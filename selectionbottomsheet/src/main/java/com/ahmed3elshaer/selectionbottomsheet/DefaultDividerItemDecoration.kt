package com.ahmed3elshaer.selectionbottomsheet

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class DefaultDividerItemDecoration(
    private val context: Context,
    orientation: Int,
    private val margin: Int
) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable?
    private var mOrientation = 0

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
        setOrientation(orientation)
    }

    private fun setOrientation(orientation: Int) {
        mOrientation = if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            VERTICAL_LIST
        } else {
            orientation
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(c: Canvas?, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + (mDivider?.intrinsicHeight ?: mDivider?.minimumHeight ?: 0)
            mDivider?.setBounds(left + dpToPx(margin), top, right, bottom)
            c?.let { mDivider?.draw(it) }
        }
    }

    private fun drawHorizontal(c: Canvas?, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + (mDivider?.intrinsicHeight ?: mDivider?.minimumHeight ?: 0)
            mDivider?.setBounds(left, top + dpToPx(margin), right, bottom - dpToPx(margin))
            c?.let { mDivider?.draw(it) }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (mOrientation == VERTICAL_LIST) {
            outRect[0, 0, 0] = mDivider?.intrinsicHeight ?: mDivider?.minimumHeight ?: 0
        } else {
            outRect[0, 0, mDivider?.intrinsicWidth ?: mDivider?.minimumWidth ?: 0] = 0
        }
    }

    private fun dpToPx(dp: Int): Int {
        val r = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        ).roundToInt()
    }

    companion object {
        private val ATTRS = intArrayOf(
            android.R.attr.listDivider
        )
        const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }
}