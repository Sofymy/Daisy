package com.example.daisy.ui.common.elements

import java.util.Locale

fun String.pluralize(count: Int, locale: Locale = Locale.getDefault()): String {
    return if (locale.language == "en" && count > 1) {
        this + 's'
    } else {
        this
    }
}
