package com.example.daisy.ui.model

data class UserUi(
    val name: String? = "",
    val email: String = "",
    val photoUrl: String? = null,
    val friends: List<String> = emptyList()
)
