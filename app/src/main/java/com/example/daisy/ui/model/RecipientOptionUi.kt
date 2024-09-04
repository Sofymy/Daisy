package com.example.daisy.ui.model

sealed class RecipientOptionUi {
    class RecipientByEmail(val user: UserUi) : RecipientOptionUi()
    class RecipientByCode(val code: CodeUi) : RecipientOptionUi()
}