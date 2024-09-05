package com.example.daisy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.daisy.navigation.NavGraph
import com.example.daisy.ui.common.navbar.BottomNavigationBar
import com.example.daisy.ui.theme.DaisyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    DaisyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.padding(innerPadding),
            ) {
                NavGraph(navController)
            }
        }
    }
}
