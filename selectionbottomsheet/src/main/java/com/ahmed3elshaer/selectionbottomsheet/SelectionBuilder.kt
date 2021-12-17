package com.ahmed3elshaer.selectionbottomsheet

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.GravityInt
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ahmed3elshaer.selectionbottomsheet.databinding.SelectionBottomSheetBinding
import com.ahmed3elshaer.selectionbottomsheet.databinding.SingleChoiceItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectionBuilder<T>(private val fragmentManager: FragmentManager) {
    private var itemsList: List<T> = mutableListOf()
    private var title: String = String()
    private var selectionItemBinder: (item: T) -> String = { String() }
    private var callback: (item: T) -> Unit = {}
    private var defaultItemBinder: ((T) -> Boolean)? = null
    private var confirmCallback: (item: T?) -> Unit = {}
    private var confirmText: String? = null
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

    constructor(activity: FragmentActivity) : this(activity.supportFragmentManager)
    constructor(fragment: Fragment) : this(fragment.childFragmentManager)

    /**
     * Call to pass the list of models that gonna be shown in the
     * RecyclerView.  it's implementation helps us use whatever model,
     * we want so we can get the same model on the selection callback.
     * @param itemsList sets the list of the models to the RecyclerView
     */
    fun list(itemsList: List<T>) = apply {
        this.itemsList = itemsList
    }

    /**
     * Call to pass title of the bottom sheet
     * @param title bottom sheet title
     */
    fun title(title: String): SelectionBuilder<T> {
        this.title = title
        return this
    }

    /**
     * Call to determine which property of the model is gonna
     * be shown in the RecyclerView's item TextView
     * by passing the Model itself to you so you can just return the String type
     * property that your models hold to be shown in the selection
     * @param selectionItemBinder lambda expression has your model
     * as a param so you can specify the string property on it as a return type.
     */
    fun itemBinder(selectionItemBinder: (item: T) -> String) = apply {
        this.selectionItemBinder = selectionItemBinder
    }

    /**
     * @param listener is a lambda expression that would trigger if user selected
     * any item of the RecyclerView with the selected item model as a param
     */
    fun selectionListener(listener: (item: T) -> Unit) = apply {
        this.callback = listener
    }

    fun defaultItem(item: T) = apply {
        defaultItemBinder = { it == item }
    }

    fun defaultItem(predicate: (T) -> Boolean) = apply {
        defaultItemBinder = predicate
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

    /**
     * Call to initialize and show BottomSheetDialogFragment
     * @param tag is optional param, if you are not gonna use the tag eg. for
     * reusing the BottomSheet again you can skip it, and a random String would
     * be set
     */
    fun show(
        tag: String
    ): BottomSheetDialogFragment {
        val dialog = createDialog()
        dialog.show(fragmentManager, tag)
        return dialog
    }

    fun show(): BottomSheetDialogFragment {
        val dialog = createDialog()
        dialog.show(fragmentManager, dialog.tag ?: TAG)
        return dialog
    }

    private fun createDialog() = SelectionBottomSheet(
        itemsList,
        title,
        selectionItemBinder,
        callback,
        defaultItemBinder,
        confirmCallback,
        confirmText,
        dragIndicatorColor,
        titleColor,
        titleGravity,
        itemColor,
        selectionColor,
        selectionDrawable,
        confirmTextColor,
        confirmBackgroundColor,
        confirmDisabledTextColor,
        confirmDisabledBackgroundColor,
        expandState
    )

    companion object {
        const val TAG = "SelectionDialog"
    }
}