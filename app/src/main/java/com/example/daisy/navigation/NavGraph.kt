package com.example.daisy.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.daisy.feature.auth.login.LoginScreen
import com.example.daisy.feature.auth.register.RegisterScreen

@ExperimentalMaterial3Api
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.Login) {
        composable<Screen.Login> {
            LoginScreen(onNavigateToRegister = {
                navController.navigate(Screen.Register)
            })
        }
        composable<Screen.Register> {
            RegisterScreen(onNavigateToLogin = {
                navController.navigate(Screen.Login)
            })
        }
    }
}
