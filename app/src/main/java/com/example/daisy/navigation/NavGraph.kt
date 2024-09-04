package com.example.daisy.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.daisy.feature.auth.register.RegisterScreen
import com.example.daisy.feature.auth.sign_in.SignInScreen
import com.example.daisy.feature.created_calendars.CreatedCalendarScreen
import com.example.daisy.feature.created_calendars.CreatedCalendarsScreen
import com.example.daisy.feature.new_calendar.NewCalendarScreen
import com.example.daisy.feature.home.HomeScreen
import com.example.daisy.feature.received_calendars.ReceivedCalendarsScreen

@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(navController, startDestination = Screen.SignIn) {

        composable<Screen.SignIn> {
            SignInScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register) },
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.SignIn) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Register> {
            RegisterScreen(onNavigateToSignIn = {
                navController.navigate(Screen.SignIn)
            })
        }

        composable<Screen.Home> {
            HomeScreen(
                onNavigateToNewCalendar = {
                    navController.navigate(Screen.NewCalendar)
                },
                onNavigateToCreatedCalendars = {
                    navController.navigate(Screen.CreatedCalendars)
                }
            )
        }

        composable<Screen.NewCalendar> {
            NewCalendarScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.NewCalendar) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.CreatedCalendars> {
            CreatedCalendarsScreen(
                onNavigateToCreatedCalendar = {
                    navController.navigate(Screen.CreatedCalendar(it))
                }
            )
        }

        composable<Screen.CreatedCalendar> {backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")

            CreatedCalendarScreen(id = id)
        }

        composable<Screen.ReceivedCalendars> {
            ReceivedCalendarsScreen()
        }

    }
}

