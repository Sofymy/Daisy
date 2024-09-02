package com.example.daisy.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.daisy.feature.auth.register.RegisterScreen
import com.example.daisy.feature.auth.sign_in.SignInScreen
import com.example.daisy.feature.create_calendar.CreateCalendarScreen
import com.example.daisy.feature.home.HomeScreen

@ExperimentalMaterial3Api
@Composable
fun NavGraph() {
    val navController = rememberNavController()

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
                onNavigateToCreateCalendar = {
                    navController.navigate(Screen.CreateCalendar)
                }
            )
        }

        composable<Screen.CreateCalendar> {
            CreateCalendarScreen()
        }

    }
}

