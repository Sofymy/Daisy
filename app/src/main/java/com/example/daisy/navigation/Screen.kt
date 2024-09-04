package com.example.daisy.navigation

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(
    val resourceId: String
){
    @Serializable
    data object SignIn: Screen("SignIn")

    @Serializable
    data object Register: Screen("Register")

    @Serializable
    data object Home: Screen("Home")

    @Serializable
    data object NewCalendar: Screen("NewCalendar")

    @Serializable
    data object CreatedCalendars: Screen("CreatedCalendars")

    @Serializable
    data class CreatedCalendar(val id: String) : Screen("CreatedCalendar")

    @Serializable
    data object ReceivedCalendars: Screen("ReceivedCalendars")
}