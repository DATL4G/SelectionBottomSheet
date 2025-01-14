package com.ahmed3elshaer.selectionbottomsheet

sealed class ExpandState {
    object Default : ExpandState()
    object Expanded : ExpandState()
    object ExpandOnTv : ExpandState()
    class ExpandCustom(val predicate: () -> Boolean) : ExpandState()
}
