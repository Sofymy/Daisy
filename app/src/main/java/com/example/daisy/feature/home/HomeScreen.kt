package com.example.daisy.feature.home

import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.daisy.R
import com.example.daisy.ui.common.elements.WavyShape
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.model.UserUi
import com.example.daisy.ui.theme.gradient2
import com.google.firebase.auth.FirebaseUser


@Composable
fun HomeScreen(
    onNavigateToNewCalendar: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCreatedCalendars: () -> Unit,
    onNavigateToReceivedCalendars: () -> Unit
) {
    HomeScreenContent(
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToCreatedCalendars = onNavigateToCreatedCalendars,
        onNavigateToReceivedCalendars = onNavigateToReceivedCalendars
    )
}

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit,
    onNavigateToCreatedCalendars: () -> Unit,
    onNavigateToReceivedCalendars: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val mockedReceivedCalendars = listOf(CalendarUi(title = "Minta naptár", recipients = listOf("MockUser1", "MockUser2"), sender = UserUi(name = "Sender1")))
    val mockedCreatedCalendars = listOf(
        CalendarUi(title = "Mocked1", recipients = listOf("MockUser1", "MockUser2")), CalendarUi(
        title = "Mocked1", recipients = listOf("MockUser2")), CalendarUi(title = "Mocked1", recipients = listOf("MockUser2")), CalendarUi(
        title = "Mocked1", recipients = listOf("MockUser2")))

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
                        HomeHeader(currentUser = state.currentUser, onNavigateToProfile = onNavigateToProfile)
                    }
                }
                item {
                    HomeReceivedCalendars(
                        receivedCalendars = state.receivedCalendars.ifEmpty { mockedReceivedCalendars },
                        navigateToReceivedCalendars = onNavigateToReceivedCalendars,
                    )
                }
                item {
                    HomeCreatedCalendars(
                        createdCalendars = state.createdCalendars,
                        navigateToCreatedCalendars = onNavigateToCreatedCalendars,
                    )
                }
            }
        }
    }
}

@Composable
fun HomeAuroraAnimation() {

    val transition = rememberInfiniteTransition(label = "")
    val animatedProgress by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOutQuart),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val brush = Brush.verticalGradient(
        colors = gradient2,
    )

    Box(Modifier
        .height(130.dp)
        .fillMaxWidth()
        .clip(WavyShape(2.dp, 10.dp * animatedProgress))
        .graphicsLayer {
            alpha = animatedProgress
        }
        .blur(30.dp, edgeTreatment = BlurredEdgeTreatment.Rectangle)
        .background(brush))

}

@Composable
fun HomeHeader(
    currentUser: FirebaseUser?,
    onNavigateToProfile: () -> Unit
) {

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
                        .border(1.dp, Color.White.copy(.1f), CircleShape)
                        .padding(4.dp)
                        .size(50.dp)
                        .clip(CircleShape)
                        .clickable {
                            onNavigateToProfile()
                        }
                    ,
                    contentScale = ContentScale.Crop,
                )

            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = stringResource(R.string.hi) + " " + currentUser.displayName.toString() + "! 👋🏻 ", style = MaterialTheme.typography.bodySmall.copy(platformStyle = PlatformTextStyle(
                    emojiSupportMatch = EmojiSupportMatch.All
                )), color = Color.LightGray)

                Text(text = stringResource(R.string.welcome_back), fontWeight = FontWeight.Bold)
            }
        }
    }
}
