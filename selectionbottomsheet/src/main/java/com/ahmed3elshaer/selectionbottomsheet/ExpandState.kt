package com.ahmed3elshaer.selectionbottomsheet

sealed class ExpandState {
    object Default : ExpandState()
    object Expanded : ExpandState()
    object ExpandOnTv : ExpandState()
}
