package com.example.daisy.ui.common.navbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.daisy.navigation.Screen
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavigationBarItem.Home,
        BottomNavigationBarItem.Calendars,
        BottomNavigationBarItem.NewCalendar,
        BottomNavigationBarItem.Community,
        BottomNavigationBarItem.Profile
    )

    val interactionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
        ,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(30, 30, 0, 0))
                .background(MediumGrey)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .fillMaxWidth() ,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items.forEach { item ->
                    NavigationItem(
                        item = item,
                        currentDestination = currentDestination,
                        navController = navController,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        Column(
            Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-25).dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) {
                        navController.navigate(Screen.NewCalendar)
                    }
                    .drawWithCache {
                        val roundedPolygon = RoundedPolygon(
                            numVertices = 6,
                            radius = size.minDimension / 2 - 0.2f,
                            centerX = size.width / 2,
                            centerY = size.height / 2,
                            rounding = CornerRounding(
                                size.minDimension / 10f,
                                smoothing = 0.1f
                            )
                        )
                        val roundedPolygonPath = roundedPolygon
                            .toPath()
                            .asComposePath()
                        onDrawBehind {
                            rotate(degrees = 30f, pivot = Offset(size.width / 2, size.height / 2)) {
                                drawPath(roundedPolygonPath, color = Color.White.copy(.5f))
                            }
                        }
                    }
                    .drawWithCache {
                        val roundedPolygon = RoundedPolygon(
                            numVertices = 6,
                            radius = size.minDimension / 2 - 1,
                            centerX = size.width / 2,
                            centerY = size.height / 2,
                            rounding = CornerRounding(
                                size.minDimension / 10f,
                                smoothing = 0.1f
                            )
                        )
                        val roundedPolygonPath = roundedPolygon
                            .toPath()
                            .asComposePath()
                        onDrawBehind {
                            rotate(degrees = 30f, pivot = Offset(size.width / 2, size.height / 2)) {
                                drawPath(roundedPolygonPath, color = Purple)
                            }
                        }
                    }
                    .size(55.dp)
            ){
                Icon(Icons.Default.Add, tint = Color.White, contentDescription = null)
            }
        }
    }
}

@Composable
fun NavigationItem(
    item: BottomNavigationBarItem,
    currentDestination: NavDestination?,
    navController: NavHostController,
    modifier: Modifier
) {
    val selected = currentDestination?.route.toString().substringAfterLast(".") == item.toString()

    val animatedColor by animateColorAsState(
        targetValue = if(selected) Purple else Color.White.copy(0.3f),
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearEasing
        ),
        label = ""
    )
    val dividerWidth = animateDpAsState(
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearEasing
        ),
        targetValue = if(selected)50.dp else 0.dp,
        label = ""
    )
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        navController.navigate(item.screen)
                    }
                )
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(item.screen.label!= "New") {
                Box(
                    Modifier
                        .clip(CircleShape)
                        .width(dividerWidth.value)
                        .background(Purple)
                        .height(3.dp))
            }
            Icon(modifier = Modifier.padding(top = 5.dp).size(24.dp), imageVector = item.icon, contentDescription = "", tint = if(item.screen.label != "New") animatedColor else Color.Transparent)
            Spacer(Modifier.height(5.dp))
            Text(text = item.screen.label, color = animatedColor, textAlign = TextAlign.Center, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Normal)
            Spacer(Modifier.height(5.dp))
        }
    }
}