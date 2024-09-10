package com.example.daisy

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.daisy.navigation.NavGraph
import com.example.daisy.navigation.Screen
import com.example.daisy.ui.common.navbar.BottomNavigationBar
import com.example.daisy.ui.common.navbar.TopNavigationBar
import com.example.daisy.ui.theme.DaisyTheme
import kotlin.random.Random

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

    // Memorize the circles so they don't get recomposed every time
    val circles = remember { generateRandomCircles(30) }

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
                if (topBarState.value)
                    TopNavigationBar(
                        navController = navController,
                        topNavigationBarTitle = topNavigationBarTitle,
                        onShowBottomSheet = {
                            showBottomSheet = true
                        },
                    )
            },
            bottomBar = {
                if (bottomBarState.value)
                    BottomNavigationBar(navController = navController)
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircles(circles)
                    }
                }
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

fun generateRandomCircles(count: Int): List<CircleData> {
    return List(count) {
        val radius = Random.nextInt(2, 8).toFloat()
        val x = Random.nextFloat()
        val y = Random.nextFloat()
        CircleData(radius, x, y)
    }
}

fun DrawScope.drawCircles(circles: List<CircleData>) {
    circles.forEach { circle ->
        drawCircle(
            color = Color.White.copy(0.4f),
            radius = circle.radius,
            center = Offset(circle.x * size.width, circle.y * size.height)
        )
    }
}

data class CircleData(
    val radius: Float,
    val x: Float,
    val y: Float
)

