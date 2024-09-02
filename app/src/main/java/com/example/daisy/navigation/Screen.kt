package com.example.daisy.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen{
    @Serializable
    object SignIn

    @Serializable
    object Register

    @Serializable
    object Home

    @Serializable
    object CreateCalendar
}