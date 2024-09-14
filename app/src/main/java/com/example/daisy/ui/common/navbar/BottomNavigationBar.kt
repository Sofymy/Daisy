package com.example.daisy.ui.common.navbar

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.daisy.ui.theme.DarkGrey
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.theme.gradient
import com.example.daisy.ui.theme.gradient2

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavigationBarItem.Home,
        BottomNavigationBarItem.CreatedCalendars,
        BottomNavigationBarItem.NewCalendar,
        BottomNavigationBarItem.ReceivedCalendars,
        BottomNavigationBarItem.More
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
        ,
    ) {
        Column(
            modifier = Modifier
                //.border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(30, 30, 0, 0))
                .clip(RoundedCornerShape(30, 30, 0, 0))
                .background(MediumGrey)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 15.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceEvenly
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
            FloatingActionButton(
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                onClick = { /*TODO*/ },
                shape = CutCornerShape(20),
                modifier = Modifier
                    .border(3.dp, Purple, CutCornerShape(20))
                    .border(7.dp, MediumGrey, CutCornerShape(20))
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = DarkGrey)
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

    val contentColor = Color.White.copy(0.5f)

    val animatedColor by animateColorAsState(
        targetValue = if(selected)contentColor else Color.White.copy(0.3f),
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
        targetValue = if(selected)40.dp else 0.dp,
        label = ""
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        navController.navigate(item.screen)
                    }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .clip(RoundedCornerShape(100))
                    .width(dividerWidth.value)
                    .background(Purple)
                    .height(3.dp))
            Icon(modifier = Modifier.padding(top = 5.dp), imageVector = item.icon, contentDescription = "", tint = if(item.screen.label != "New") animatedColor else Color.Transparent)
            Spacer(Modifier.height(5.dp))
            Text(text = item.screen.label, color = animatedColor, textAlign = TextAlign.Center, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Normal)
            Spacer(Modifier.height(5.dp))
        }
    }
}