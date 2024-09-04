package com.example.daisy.domain.model

import com.example.daisy.ui.model.UserUi

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = ""
)

fun UserUi.toDomain(): User {
    return User(
        name = name,
        email = email
    )
}

fun User.toUi(): UserUi {
    return UserUi(
        name = this.name,
        email = this.email
    )
}