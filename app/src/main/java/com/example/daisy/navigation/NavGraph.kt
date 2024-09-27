package com.example.daisy.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.daisy.feature.auth.onboarding.OnboardingScreen
import com.example.daisy.feature.auth.register.RegisterScreen
import com.example.daisy.feature.auth.sign_in.SignInScreen
import com.example.daisy.feature.calendars.CalendarsScreen
import com.example.daisy.feature.calendars.created_calendars.CreatedCalendarEditorScreen
import com.example.daisy.feature.calendars.created_calendars.CreatedCalendarsScreen
import com.example.daisy.feature.new_calendar.NewCalendarScreen
import com.example.daisy.feature.home.HomeScreen
import com.example.daisy.feature.calendars.received_calendars.ReceivedCalendarsScreen
import com.example.daisy.feature.community.CommunityScreen
import com.example.daisy.feature.profile.ProfileScreen
import com.example.daisy.feature.profile.account.ProfileAccountScreen

@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    navController: NavHostController,
    onTopNavigationBarTitleChange: (String) -> Unit
) {
    NavHost(navController, startDestination = Screen.Onboarding) {

        composable<Screen.Onboarding> {

            OnboardingScreen(
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register) },
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.SignIn) { inclusive = true }
                    }
                }
            )
        }

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
            onTopNavigationBarTitleChange("Home")

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
            onTopNavigationBarTitleChange("")

            NewCalendarScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.NewCalendar) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Calendars> {
            onTopNavigationBarTitleChange("Calendars")

            CalendarsScreen(
                onNavigateToCreatedCalendar = {
                    navController.navigate(Screen.CreatedCalendarEditor(it))
                }
            )
        }

        composable<Screen.Profile> {
            onTopNavigationBarTitleChange("Profile")

            ProfileScreen(
                onNavigateToProfileAccount = {
                    navController.navigate(Screen.ProfileAccount)
                },
                onNavigateToOnBoarding = {
                    navController.navigate(Screen.Onboarding) {
                        popUpTo(Screen.Home) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.ProfileAccount> {
            onTopNavigationBarTitleChange("Account settings")

            ProfileAccountScreen(
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile)
                }
            )
        }

        composable<Screen.Community> {
            onTopNavigationBarTitleChange("Community")

            CommunityScreen()
        }

        composable<Screen.CreatedCalendars> {
            onTopNavigationBarTitleChange("Created calendars")

            CreatedCalendarsScreen(
                onNavigateToCreatedCalendar = {
                    navController.navigate(Screen.CreatedCalendarEditor(it))
                }
            )
        }

        composable<Screen.CreatedCalendarEditor> { backStackEntry ->
            onTopNavigationBarTitleChange("Calendar preview")

            val id = backStackEntry.arguments?.getString("id")
            CreatedCalendarEditorScreen(id = id)
        }

        composable<Screen.ReceivedCalendars> {
            onTopNavigationBarTitleChange("Received calendars")

            ReceivedCalendarsScreen()
        }

    }
}

