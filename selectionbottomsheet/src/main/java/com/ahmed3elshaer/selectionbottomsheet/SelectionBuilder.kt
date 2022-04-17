package com.ahmed3elshaer.selectionbottomsheet

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.annotation.GravityInt
import java.security.SecureRandom

class SelectionBuilder<T> {
    private val id: String = SecureRandom().uuid()
    private var itemsList: List<T> = mutableListOf()
    private var title: String = String()
    private var itemBinder: (item: T) -> String = { it.toString() }
    private var selectionCallback: (item: T) -> Unit = {}
    private var defaultItemConfirmable: Boolean = false
    private var defaultItemBinder: ((T) -> Boolean)? = null
    private var confirmCallback: (item: T?) -> Unit = {}
    private var confirmText: String = String()
    private var dragIndicatorColor: Int? = null
    private var titleColor: Int = Color.WHITE
    private var titleGravity: Int = Gravity.CENTER
    private var itemColor: Int = Color.WHITE
    private var selectionColor: Int = Color.GREEN
    private var selectionDrawable: Drawable? = null
    private var confirmTextColor: Int = Color.BLACK
    private var confirmBackgroundColor: Int = Color.WHITE
    private var confirmDisabledTextColor: Int = Color.WHITE
    private var confirmDisabledBackgroundColor: Int = Color.TRANSPARENT
    private var expandState: ExpandState = ExpandState.Default

    fun list(itemsList: List<T>) = apply {
        this.itemsList = itemsList
    }

    fun list(vararg items: T) = list(items.toList())

    fun title(title: String) = apply {
        this.title = title
    }

    fun itemBinder(itemBinder: (item: T) -> String) = apply {
        this.itemBinder = itemBinder
    }

    fun selectionListener(listener: (item: T) -> Unit) = apply {
        this.selectionCallback = listener
    }

    fun defaultItem(item: T) = apply {
        defaultItemBinder = { it == item }
    }

    fun defaultItem(predicate: (T) -> Boolean) = apply {
        defaultItemBinder = predicate
    }

    fun defaultItemFirst() = apply {
        defaultItemBinder = { it != null && itemsList.isNotEmpty() && it == itemsList.first() }
    }

    fun defaultItemConfirmable(confirmable: Boolean = true) = apply {
        defaultItemConfirmable = confirmable
    }

    fun confirmListener(listener: (item: T?) -> Unit) = apply {
        this.confirmCallback = listener
    }

    fun confirmText(text: String) = apply {
        this.confirmText = text
    }

    fun dragIndicatorColor(@ColorInt color: Int?) {
        dragIndicatorColor = color
    }

    fun titleColor(@ColorInt color: Int) = apply {
        titleColor = color
    }

    fun titleGravity(@GravityInt gravity: Int) = apply {
        titleGravity = gravity
    }

    fun itemColor(@ColorInt color: Int) = apply {
        itemColor = color
    }

    fun selectionColor(@ColorInt color: Int) = apply {
        selectionColor = color
    }

    fun selectionDrawable(drawable: Drawable?) = apply {
        selectionDrawable = drawable
    }

    fun confirmTextColor(@ColorInt color: Int) = apply {
        confirmTextColor = color
    }

    fun confirmDisabledTextColor(@ColorInt color: Int) = apply {
        confirmDisabledTextColor = color
    }

    fun confirmBackgroundColor(@ColorInt color: Int) = apply {
        confirmBackgroundColor = color
    }

    fun confirmDisabledBackgroundColor(@ColorInt color: Int) = apply {
        confirmDisabledBackgroundColor = color
    }

    fun setExpandState(state: ExpandState) = apply {
        expandState = state
    }

    fun build(): SelectionBottomSheet<T> {
        SelectData.itemList[id] = itemsList
        SelectData.selectionDrawable[id] = selectionDrawable
        SelectData.expandState[id] = expandState
        return SelectionBottomSheet.newInstance(
            SelectData(
                id,
                dragIndicatorColor,
                title,
                titleColor,
                titleGravity,
                itemColor,
                selectionColor,
                defaultItemConfirmable,
                confirmText,
                confirmTextColor,
                confirmBackgroundColor,
                confirmDisabledTextColor,
                confirmDisabledBackgroundColor,
                itemBinder,
                selectionCallback,
                defaultItemBinder,
                confirmCallback
            ),
        )
    }
}