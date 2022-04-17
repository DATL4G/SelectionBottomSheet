package com.ahmed3elshaer.selectionbottomsheet

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.view.Gravity
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
internal data class SelectData<T> internal constructor(
    val id: String,
    val dragIndicatorColor: Int? = null,
    val title: String,
    val titleColor: Int = Color.BLACK,
    val titleGravity: Int = Gravity.CENTER,
    val itemColor: Int = Color.BLACK,
    val selectionColor: Int = Color.GREEN,
    val defaultItemConfirmable: Boolean = true,
    val confirmText: String,
    val confirmTextColor: Int = Color.WHITE,
    val confirmBackgroundColor: Int = Color.BLACK,
    val confirmDisabledTextColor: Int = Color.BLACK,
    val confirmDisabledBackgroundColor: Int = Color.WHITE,

    val itemBinder: (item: T) -> String,
    val selectionCallback: (item: T) -> Unit,
    val defaultItemBinder: ((item: T) -> Boolean)?,
    val confirmCallback: (item: T?) -> Unit
): Parcelable {
    companion object {
        internal val itemList: MutableMap<String, List<Any?>> = mutableMapOf()
        internal val selectionDrawable: MutableMap<String, Drawable?> = mutableMapOf()
        internal val expandState: MutableMap<String, ExpandState> = mutableMapOf()
    }
}
