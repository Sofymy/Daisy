package com.example.daisy.domain.model

import com.example.daisy.ui.model.UserUi

data class User(
    val uid: String = "",
    val name: String? = "",
    val email: String = "",
    val photoUrl: String? = null,
    val friends: List<String> = emptyList()
)

fun UserUi.toDomain(): User {
    return User(
        name = name,
        email = email,
        photoUrl = photoUrl,
        friends = friends
    )
}

fun User.toUi(): UserUi {
    return UserUi(
        name = this.name,
        email = this.email,
        photoUrl = this.photoUrl,
        friends = this.friends
    )
}