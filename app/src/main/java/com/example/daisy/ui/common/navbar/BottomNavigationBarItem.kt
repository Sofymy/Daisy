package com.example.daisy.ui.common.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.BorderAll
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.daisy.navigation.Screen
import kotlinx.serialization.Serializable

sealed class BottomNavigationBarItem(
    val screen: Screen,
    val icon: ImageVector
) {
    data object Home : BottomNavigationBarItem(
        screen = Screen.Home,
        icon = Icons.Outlined.Home
    )

    data object NewCalendar : BottomNavigationBarItem(
        screen = Screen.NewCalendar,
        icon = Icons.Outlined.Add
    )

    data object CreatedCalendars : BottomNavigationBarItem(
        screen = Screen.CreatedCalendars,
        icon = Icons.Outlined.CalendarToday
    )

    data object ReceivedCalendars : BottomNavigationBarItem(
        screen = Screen.ReceivedCalendars,
        icon = Icons.Outlined.CardGiftcard
    )

    data object More : BottomNavigationBarItem(
        screen = Screen.More,
        icon = Icons.Outlined.BorderAll
    )

}