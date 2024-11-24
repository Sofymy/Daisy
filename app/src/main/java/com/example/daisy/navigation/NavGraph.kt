package com.example.daisy.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.daisy.R
import com.example.daisy.feature.auth.onboarding.OnboardingScreen
import com.example.daisy.feature.auth.register.RegisterScreen
import com.example.daisy.feature.auth.sign_in.SignInScreen
import com.example.daisy.feature.calendars.CalendarsScreen
import com.example.daisy.feature.calendars.created_calendars.CreatedCalendarEditorDayScreen
import com.example.daisy.feature.calendars.created_calendars.CreatedCalendarEditorScreen
import com.example.daisy.feature.calendars.received_calendars.ReceivedCalendarDayScreen
import com.example.daisy.feature.calendars.received_calendars.ReceivedCalendarScreen
import com.example.daisy.feature.new_calendar.NewCalendarScreen
import com.example.daisy.feature.home.HomeScreen
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
                    navController.navigate(Screen.SignIn) },
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
            onTopNavigationBarTitleChange(stringResource(R.string.home))

            HomeScreen(
                onNavigateToNewCalendar = {
                    navController.navigate(Screen.NewCalendar)
                },
                onNavigateToCreatedCalendars = {
                    navController.navigate(Screen.Calendars(initialPage = 1))
                },
                onNavigateToReceivedCalendars = {
                    navController.navigate(Screen.Calendars(initialPage = 0))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile)
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

        composable<Screen.Calendars> {backStackEntry ->
            onTopNavigationBarTitleChange("")

            val initialPage = backStackEntry.arguments?.getInt("initialPage")

            CalendarsScreen(
                onNavigateToCreatedCalendar = {
                    navController.navigate(Screen.CreatedCalendarEditor(it))
                },
                onNavigateToReceivedCalendar = {
                    navController.navigate(Screen.ReceivedCalendar(it))
                },
                initialPage = initialPage ?: 0
            )
        }

        composable<Screen.Profile> {
            onTopNavigationBarTitleChange(stringResource(R.string.profile))

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
            onTopNavigationBarTitleChange(stringResource(R.string.account_settings))

            ProfileAccountScreen(
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile)
                }
            )
        }

        composable<Screen.Community> {
            onTopNavigationBarTitleChange(stringResource(R.string.community))

            CommunityScreen()
        }

        composable<Screen.ReceivedCalendar> { backStackEntry ->
            onTopNavigationBarTitleChange(stringResource(R.string.calendar))

            val id = backStackEntry.arguments?.getString("id")
            ReceivedCalendarScreen(
                id = id,
                onNavigateToReceivedCalendarDay = { number ->
                    if (id != null) {
                        navController.navigate(Screen.ReceivedCalendarDay(id, number))
                    }
                }
            )
        }

        composable<Screen.ReceivedCalendarDay> { backStackEntry ->
            onTopNavigationBarTitleChange(stringResource(R.string.calendar))

            val id = backStackEntry.arguments?.getString("id")
            val number = backStackEntry.arguments?.getInt("number")

            ReceivedCalendarDayScreen(
                id = id,
                number = number
            )
        }

        composable<Screen.CreatedCalendarEditor> { backStackEntry ->
            onTopNavigationBarTitleChange(stringResource(R.string.calendar))

            val id = backStackEntry.arguments?.getString("id")
            CreatedCalendarEditorScreen(
                id = id,
                onNavigateToCreatedCalendarEditorDay = { number ->
                    if (id != null) {
                        navController.navigate(Screen.CreatedCalendarEditorDay(id, number))
                    }
                }
            )
        }

        composable<Screen.CreatedCalendarEditorDay> { backStackEntry ->
            onTopNavigationBarTitleChange(stringResource(R.string.calendar))

            val id = backStackEntry.arguments?.getString("id")
            val number = backStackEntry.arguments?.getInt("number")
            CreatedCalendarEditorDayScreen(
                id = id,
                number = number
            )
        }

    }
}

