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
    data class CreatedCalendarEditor(val id: String) : Screen("CreatedCalendar", "Created preview")

    @Serializable
    data class CreatedCalendarEditorDay(val id: String, val number: Int) : Screen("CreatedCalendarDay", "Created preview")

    @Serializable
    data object ReceivedCalendars: Screen("ReceivedCalendars", "Received")

    @Serializable
    data class ReceivedCalendar(val id: String) : Screen("ReceivedCalendar", "Created preview")

    @Serializable
    data class ReceivedCalendarDay(val id: String, val number: Int) : Screen("ReceivedCalendarDay", "Created preview")

    @Serializable
    data object More: Screen("More", "More")

    @Serializable
    data object Profile: Screen("Profile", "Profile")

    @Serializable
    data object ProfileAccount: Screen("Account", "Account")

    @Serializable
    data class Calendars(val initialPage: Int): Screen("Calendars", "Calendars")

    @Serializable
    data object Community: Screen("Community", "Community")

}