package com.example.daisy.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.daisy.R
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
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
        onResume = {
            viewModel.onEvent(HomeUserEvent.GetCurrentUser)
            viewModel.onEvent(HomeUserEvent.GetCreatedCalendars)
            viewModel.onEvent(HomeUserEvent.GetReceivedCalendars)
        },
    )

    when {
        state.isLoading -> LoadingContent()
        else -> {
            LazyColumn(
                Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        contentAlignment = Alignment.TopCenter
                    ) {
                        HomeAuroraAnimation()
                        HomeHeader(state.currentUser)
                    }
                }
                item {
                    HomeReceivedCalendars(state.receivedCalendars)
                }
                item {
                    HomeCreatedCalendars(state.createdCalendars)
                }
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
                .padding(horizontal = 20.dp, vertical = 30.dp)
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentUser.photoUrl)
                        .placeholder(R.drawable.gift)
                        .build(),
                    contentDescription = "",
                    modifier = Modifier
                        .border(2.dp, Color.White, CircleShape)
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Box(modifier = Modifier
                    .offset(x = 5.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(3.dp)
                    ,
                    contentAlignment = Alignment.Center
                ){
                    Icon(imageVector = Icons.Outlined.Favorite, contentDescription = null, tint = Color.Black)
                }
            }
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
