package com.example.daisy.ui.model

data class DesignUi(
    val design: DesignOption = DesignOption.entries.toTypedArray().random()
)

enum class DesignOption{
    CHRISTMAS,
    BIRTHDAY
}