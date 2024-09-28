package com.example.daisy.ui.model

import android.graphics.Bitmap
import kotlin.random.Random

data class CalendarUi(
    val id: String = Random.nextInt().toString(),
    val title: String = "",
    var drawing: Bitmap? = null,
    val icon: IconOptionUi = IconOptionUi.LOVE,
    val dateRange: DateRangeUi = DateRangeUi(),
    val recipients: List<String> = listOf(""),
    val sender: UserUi = UserUi(),
    val code: String? = null
)