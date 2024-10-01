package com.example.daisy.ui.common.navbar

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.daisy.ui.theme.DarkGrey
import com.example.daisy.ui.theme.MediumGrey
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    navController: NavHostController,
    topNavigationBarTitle: String,
    onShowBottomSheet: () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val show = remember { mutableStateOf(false) }

    val backgroundColor = animateColorAsState(
        targetValue = if((currentDestination?.route?.substringAfterLast(".") in listOf("Calendars/{resourceId}/{label}/{initialPage}")) || currentDestination?.route == null) MediumGrey else DarkGrey,
        label = "",
        animationSpec = tween(10)
    )

    LaunchedEffect(currentDestination?.route) {
        delay(1500)
        show.value = true
    }

    Column {
        TopAppBar(
            modifier = Modifier
                .shadow(0.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor.value,
                titleContentColor = Color.White,
            ),
            title = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AnimatedContent(
                        targetState = topNavigationBarTitle,
                        transitionSpec = {
                            fadeIn(tween(durationMillis = 100)) togetherWith fadeOut(tween(durationMillis = 100))
                        }, label = ""
                    ) { targetTitle ->
                        Text(
                            text = targetTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {

                }
            },
            actions = {
                Row {
                    IconButton(onClick = { }) {

                    }
                }
            }
        )
    }
}