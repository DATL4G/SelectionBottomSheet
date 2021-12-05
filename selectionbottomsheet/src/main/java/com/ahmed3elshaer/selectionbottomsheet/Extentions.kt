package com.ahmed3elshaer.selectionbottomsheet

import android.content.res.ColorStateList
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun <T> FragmentActivity.selectionBottomSheet(builder: SelectionBuilder<T>.() -> Unit) = SelectionBuilder<T>(this).apply(builder).show()
fun <T> FragmentActivity.selectionBottomSheet(tag: String, builder: SelectionBuilder<T>.() -> Unit) = SelectionBuilder<T>(this).apply(builder).show(tag)

fun <T> Fragment.selectionBottomSheet(builder: SelectionBuilder<T>.() -> Unit) = SelectionBuilder<T>(this).apply(builder).show()
fun <T> Fragment.selectionBottomSheet(tag: String, builder: SelectionBuilder<T>.() -> Unit) = SelectionBuilder<T>(this).apply(builder).show(tag)

internal fun View.hide() {
    this.visibility = View.GONE
}

internal fun View.show() {
    this.visibility = View.VISIBLE
}

internal fun colorStateListOf(vararg mapping: Pair<IntArray, Int>): ColorStateList {
    val (states, colors) = mapping.unzip()
    return ColorStateList(states.toTypedArray(), colors.toIntArray())
}