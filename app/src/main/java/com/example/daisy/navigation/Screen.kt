package com.example.daisy.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen{
    @Serializable
    object Login

    @Serializable
    object Register
}