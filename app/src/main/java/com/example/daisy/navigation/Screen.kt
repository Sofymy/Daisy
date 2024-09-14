package com.example.daisy.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(
    val resourceId: String,
    val label: String,
){
    @Serializable
    data object Onboarding: Screen("Onboarding", "Onboarding")

    @Serializable
    data object SignIn: Screen("SignIn", "Sign in")

    @Serializable
    data object Register: Screen("Register", "Register")

    @Serializable
    data object Home: Screen("Home", "Home")

    @Serializable
    data object NewCalendar: Screen("NewCalendar", "New")

    @Serializable
    data object CreatedCalendars: Screen("CreatedCalendars", "Created")

    @Serializable
    data class CreatedCalendar(val id: String) : Screen("CreatedCalendar", "Created preview")

    @Serializable
    data object ReceivedCalendars: Screen("ReceivedCalendars", "Received")

    @Serializable
    data object More: Screen("More", "More")

}