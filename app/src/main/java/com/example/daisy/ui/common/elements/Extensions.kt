package com.example.daisy.ui.common.elements

fun String.pluralize(count: Int): String {
    return if (count > 1) {
        this + 's'
    } else {
        this
    }
}