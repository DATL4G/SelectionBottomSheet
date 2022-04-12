package com.ahmed3elshaer.selectionbottomsheet

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.view.Gravity
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
internal data class SelectData<T> internal constructor(
    val dragIndicatorColor: Int? = null,
    val title: String,
    val titleColor: Int = Color.BLACK,
    val titleGravity: Int = Gravity.CENTER,
    val itemList: @RawValue List<T> = emptyList(),
    val itemColor: Int = Color.BLACK,
    val selectionColor: Int = Color.GREEN,
    val selectionDrawable: @RawValue Drawable? = null,
    val defaultItemConfirmable: Boolean = true,
    val confirmText: String,
    val confirmTextColor: Int = Color.WHITE,
    val confirmBackgroundColor: Int = Color.BLACK,
    val confirmDisabledTextColor: Int = Color.BLACK,
    val confirmDisabledBackgroundColor: Int = Color.WHITE,

    val expandState: @RawValue ExpandState = ExpandState.Default,
    val itemBinder: (item: T) -> String = { it.toString() },
    val selectionCallback: (item: T) -> Unit = { },
    val defaultItemBinder: ((item: T) -> Boolean)? = null,
    val confirmCallback: (item: T?) -> Unit = { }
): Parcelable
