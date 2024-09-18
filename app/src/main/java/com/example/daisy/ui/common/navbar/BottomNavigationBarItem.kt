package com.example.daisy.ui.common.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.BorderAll
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material.icons.outlined.TagFaces
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

    data object Calendars : BottomNavigationBarItem(
        screen = Screen.Calendars,
        icon = Icons.Outlined.CalendarToday
    )

    data object Community : BottomNavigationBarItem(
        screen = Screen.Community,
        icon = Icons.Outlined.TagFaces
    )

    data object Profile : BottomNavigationBarItem(
        screen = Screen.Profile,
        icon = Icons.Outlined.ManageAccounts
    )

}