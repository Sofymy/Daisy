package com.example.daisy.domain.model

import com.example.daisy.ui.model.CodeUi
import com.example.daisy.ui.model.UserUi
import kotlin.random.Random

data class Code(
    val code: Int = Random.nextInt()
)

fun CodeUi.toDomain(): Code {
    return Code(
        code = code
    )
}

fun Code.toUi(): CodeUi {
    return CodeUi(
        code = code
    )
}