package com.example.daisy.feature.home

import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.handwriting.handwritingDetector
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.daisy.R
import com.example.daisy.ui.common.for_later_use.OnboardingAurora
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.theme.Blue
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.theme.gradient
import com.google.android.material.shape.Shapeable
import com.google.firebase.auth.FirebaseUser


@Composable
fun HomeScreen(
    onNavigateToNewCalendar: () -> Unit,
    onNavigateToCreatedCalendars: () -> Unit
) {
    HomeScreenContent()
}

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = { viewModel.onEvent(HomeUserEvent.GetCurrentUser) }
    )

    when {
        state.isLoading -> LoadingContent()
        else -> {
            Column(
                Modifier
                    .fillMaxSize()) {
                Box {
                    OnboardingAurora()
                    HomeHeader(state.currentUser)
                }
                HomeReceivedCalendars()
                HomeCreatedCalendars()
            }
        }
    }
}


@Composable
fun HomeCreatedCalendars() {
    Spacer(modifier = Modifier.height(40.dp))
    LazyRow {
        item { Spacer(modifier = Modifier.width(20.dp)) }
        items(10){
            Box(
                Modifier
                    .clip(RoundedCornerShape(10))
                    .background(MediumGrey)
                    .width(150.dp)
                    .height(150.dp)
            ){
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20))
                        .background(Color.White.copy(0.15f))
                ) {
                    Text(text = "${it+4}th Sept", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(3.dp))
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

@Composable
fun HomeReceivedCalendars() {
    Box(
        Modifier
            .pointerInput(Unit){
                detectDragGestures { change, dragAmount ->
                    Log.d("eeeeeeee", change.toString())
                    Log.d("eeeeeeee", dragAmount.toString())
                }
            }
            .padding(20.dp)
    ) {
        Box(
            Modifier
                .graphicsLayer {
                    scaleX = 0.9f
                }
                .offset(y = 10.dp)
                .clip(RoundedCornerShape(30.dp))
                .alpha(0.7f)
                .background(Purple)
                .fillMaxWidth()
                .height(200.dp)
        )
        Box(
            Modifier
                .graphicsLayer {
                    scaleX = 0.8f
                }
                .offset(y = 20.dp)
                .clip(RoundedCornerShape(30.dp))
                .alpha(0.4f)
                .background(Purple)
                .fillMaxWidth()
                .height(200.dp)
        )
        Column(
            Modifier
                .blur(20.dp)
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(MediumGrey)
                .fillMaxWidth()
                .height(160.dp)
        ){
            Text(text = "10th Sept", style = MaterialTheme.typography.bodyLarge, modifier = Modifier
                .padding(top = 10.dp), fontWeight = FontWeight.Bold)
        }
        Column(
            Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(Purple)
                .fillMaxWidth()
                .height(200.dp)
        ){
            Text(text = "10th Sept", style = MaterialTheme.typography.bodyLarge, modifier = Modifier
                .padding(top = 10.dp), fontWeight = FontWeight.Bold)
        }
    }
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(3) { iteration ->
            val color = if (0 == iteration) Color.LightGray else Color.DarkGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .width(if (0 == iteration) 20.dp else 10.dp)
                    .background(color)
                    .height(10.dp)
            ){
            }
        }
    }
}

@Composable
fun HomeHeader(currentUser: FirebaseUser?) {
    if (currentUser != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(20.dp)
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentUser.photoUrl)
                    .placeholder(R.drawable.gift)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .border(2.dp, Brush.linearGradient(gradient), CircleShape)
                    .padding(4.dp)
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = "Hi " + currentUser.displayName.toString() + " 👋🏻 ", style = MaterialTheme.typography.bodySmall.copy(platformStyle = PlatformTextStyle(
                    emojiSupportMatch = EmojiSupportMatch.All
                )), color = Color.LightGray)

                Text(text = "Welcome Back!", fontWeight = FontWeight.Bold)
            }
        }
    }
}


