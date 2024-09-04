package com.example.daisy.ui.common

import com.example.daisy.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomNavigationBarItem(
    val screen: Screen,
) {
    @Serializable
    data object Home : BottomNavigationBarItem(
        screen = Screen.Home,
    )

    @Serializable
    data object NewCalendar : BottomNavigationBarItem(
        screen = Screen.NewCalendar,
    )

    @Serializable
    data object CreatedCalendars : BottomNavigationBarItem(
        screen = Screen.CreatedCalendars,
    )

}