package com.example.daisy.domain.util

import com.example.daisy.domain.model.Code
import com.example.daisy.domain.model.User
import com.example.daisy.domain.model.toDomain
import com.example.daisy.domain.model.toUi
import com.example.daisy.ui.model.RecipientOptionUi

sealed class RecipientOption {
    class RecipientByEmail(val user: User) : RecipientOption()
    class RecipientByCode(val code: Code) : RecipientOption()
}

fun RecipientOptionUi.toDomain(): RecipientOption {
    return when(this){
        is RecipientOptionUi.RecipientByEmail -> {
            RecipientOption.RecipientByEmail(user = this.user.toDomain())
        }
        is RecipientOptionUi.RecipientByCode -> {
            RecipientOption.RecipientByCode(code = this.code.toDomain())
        }
    }
}

fun RecipientOption.toUi(): RecipientOptionUi {
    return when(this){
        is RecipientOption.RecipientByEmail -> {
            RecipientOptionUi.RecipientByEmail(user = this.user.toUi())
        }
        is RecipientOption.RecipientByCode -> {
            RecipientOptionUi.RecipientByCode(code = this.code.toUi())
        }
    }
}