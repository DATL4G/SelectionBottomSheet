package com.ahmed3elshaer.selectionbottomsheet

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun <T> selectionBottomSheet(builder: SelectionBuilder<T>.() -> Unit) = SelectionBuilder<T>().apply(builder).build()

internal fun View.hide() {
    this.visibility = View.GONE
}

internal fun View.show() {
    this.visibility = View.VISIBLE
}

internal fun ImageView.clearTint() {
    this.clearColorFilter()
    ImageViewCompat.setImageTintList(this, null)
}

internal fun colorStateListOf(vararg mapping: Pair<IntArray, Int>): ColorStateList {
    val (states, colors) = mapping.unzip()
    return ColorStateList(states.toTypedArray(), colors.toIntArray())
}

internal fun PackageManager.isTelevision(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.hasSystemFeature(PackageManager.FEATURE_TELEVISION) || this.hasSystemFeature(
            PackageManager.FEATURE_LEANBACK
        ) || this.hasSystemFeature(PackageManager.FEATURE_LEANBACK_ONLY)
    } else {
        this.hasSystemFeature(PackageManager.FEATURE_TELEVISION) || this.hasSystemFeature(
            PackageManager.FEATURE_LEANBACK
        )
    }
}

internal fun DialogInterface.expand() {
    if (this is BottomSheetDialog) {
        val bottomSheet = this
        val sheetInternal: View? = bottomSheet.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        sheetInternal?.let { sheet ->
            sheet.post {
                BottomSheetBehavior.from(sheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
}