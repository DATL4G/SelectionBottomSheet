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
import java.util.*
import kotlin.experimental.and
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log

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

internal fun Random.uuid(): String {
    val alphabet = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    val size = 10
    val mask: Int = (2 shl floor(log((alphabet.size - 1).toDouble(), 2.0)).toInt()) - 1
    val step: Int = ceil(1.6 * mask * size / alphabet.size).toInt()
    val idBuilder = StringBuilder()

    while (true) {
        val bytes = ByteArray(step)
        this.nextBytes(bytes)

        for(i in 0 until step) {
            val alphabetIndex: Int = (bytes[i] and mask.toByte()).toInt()

            if(alphabetIndex < alphabet.size) {
                idBuilder.append(alphabet[alphabetIndex])

                if(idBuilder.length == size) {
                    return idBuilder.toString()
                }
            }
        }
    }
}