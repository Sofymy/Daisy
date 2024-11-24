package com.example.daisy.navigation

import androidx.annotation.StringRes
import com.example.daisy.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(
    val resourceId: String,
    val label: String,
    @StringRes val bottomNavigationLabelResourceId: Int = 0,
){
    @Serializable
    data object Onboarding: Screen("Onboarding", "Onboarding")

    @Serializable
    data object SignIn: Screen("SignIn", "Sign in")

    @Serializable
    data object Register: Screen("Register", "Register")

    @Serializable
    data object Home: Screen("Home", "Home", R.string.home_)

    @Serializable
    data object NewCalendar: Screen("NewCalendar", "New", R.string.new_)

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
    data object Profile: Screen("Profile", "Profile", R.string.profile_)

    @Serializable
    data object ProfileAccount: Screen("Account", "Account")

    @Serializable
    data class Calendars(val initialPage: Int): Screen("Calendars", "Calendars", R.string.calendars)

    @Serializable
    data object Community: Screen("Community", "Community", R.string.community)

}