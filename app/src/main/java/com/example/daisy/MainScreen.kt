package com.example.daisy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.daisy.navigation.NavGraph
import com.example.daisy.navigation.Screen
import com.example.daisy.ui.common.navbar.BottomNavigationBar
import com.example.daisy.ui.common.navbar.TopNavigationBar
import com.example.daisy.ui.theme.DaisyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var topNavigationBarTitle by remember {
        mutableStateOf("")
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val topBarState = rememberSaveable { (mutableStateOf(false)) }

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        when (navBackStackEntry?.destination?.route) {
            null, Screen.SignIn::class.qualifiedName.toString(), Screen.Register::class.qualifiedName.toString(), Screen.Onboarding::class.qualifiedName.toString(), -> {
                bottomBarState.value = false
                topBarState.value = false
            }
            else -> {
                bottomBarState.value = true
                topBarState.value = true
            }
        }
    }

    DaisyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if(topBarState.value)
                    TopNavigationBar(
                        navController = navController,
                        topNavigationBarTitle = topNavigationBarTitle,
                        onShowBottomSheet = {
                            showBottomSheet = true
                        },
                    )
            },
            bottomBar = {
                if(bottomBarState.value)
                    BottomNavigationBar(navController = navController)
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding),
            ) {
                NavGraph(
                    navController = navController,
                    onTopNavigationBarTitleChange = {
                        topNavigationBarTitle = it
                    }
                )
            }
        }
    }
}
