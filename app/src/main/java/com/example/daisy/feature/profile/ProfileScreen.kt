package com.example.daisy.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.daisy.R
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.theme.Alert
import com.example.daisy.ui.theme.DarkBlue
import com.example.daisy.ui.theme.DarkGrey
import com.example.daisy.ui.theme.DarkPurple
import com.example.daisy.ui.theme.LightBlue
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileScreen(
    onNavigateToProfileAccount: () -> Unit,
    onNavigateToOnBoarding: () -> Unit
) {
    ProfileScreenContent(
        onNavigateToProfileAccount = onNavigateToProfileAccount,
        onNavigateToOnBoarding = onNavigateToOnBoarding
    )
}

@Composable
fun ProfileScreenContent(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToProfileAccount: () -> Unit,
    onNavigateToOnBoarding: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = {
            viewModel.onEvent(ProfileUserEvent.GetCurrentUser)
            viewModel.onEvent(ProfileUserEvent.GetCreatedCalendars)
            viewModel.onEvent(ProfileUserEvent.GetReceivedCalendars)
        }
    )

    when {
        state.isLoading -> LoadingContent()
        else -> {
            LazyColumn(
                Modifier.fillMaxSize()
            ) {
                item {
                    ProfileHeader(currentUser = state.currentUser)
                }
                item {
                    ProfileStatistics(
                        createdCalendarsSize = state.createdCalendars.size,
                        receivedCalendarsSize = state.receivedCalendars.size,
                        friendsSize = state.friends.size
                    )
                }
                item {
                    ProfileItem(
                        modifier = Modifier.padding(top = 20.dp),
                        title = stringResource(R.string.badges),
                        content = { ProfileBadges() }
                    )
                }
                item {
                    ProfileItem(
                        modifier = Modifier.padding(top = 20.dp),
                        title = stringResource(R.string.settings),
                        content = { ProfileSettings(
                            onNavigateToProfileAccount = onNavigateToProfileAccount
                        ) }
                    )
                }
                item {
                    ProfileSignOut(
                        onClickSignOut = {
                            viewModel.onEvent(ProfileUserEvent.SignOut)
                            onNavigateToOnBoarding()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileSignOut(
    onClickSignOut: () -> Unit
) {
    Column(
        Modifier
            .padding(top = 15.dp)
            .clickable { onClickSignOut() }
            .background(Alert.copy(.2f))
            .fillMaxWidth()
            .padding(15.dp)
    ){
        Text(text = stringResource(R.string.sign_out), color = Alert, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
    }
    HorizontalDivider(color = Alert.copy(.4f))
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun ProfileItem(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ()-> Unit,
){
    Column(
        modifier
            .padding(top = 30.dp, bottom = 0.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 15.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
        ) {
            content()
        }
    }
}

@Composable
fun ProfileBadges() {
    val colors = listOf(DarkBlue, DarkPurple, LightBlue, Purple, DarkBlue)

    LazyRow {
        items(10){
            Box(modifier = Modifier
                .padding(start = 15.dp)
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
                            drawPath(roundedPolygonPath, color = Color.White.copy(.3f))
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
                            drawPath(roundedPolygonPath, Purple)
                        }
                    }
                }
                .size(100.dp)
            )
        }
    }
}

@Composable
fun ProfileSettings(
    onNavigateToProfileAccount: () -> Unit
) {
    ProfileSettingItem(label = { Text(stringResource(R.string.account)) }, onClick = onNavigateToProfileAccount)
    ProfileSettingItem(label = { Text(stringResource(R.string.notifications)) }, onClick = {  })
}

@Composable
fun ProfileSettingItem(
    label: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .clickable {
                onClick()
            }
            .background(MediumGrey)
    ) {
        Row(
            Modifier
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(Modifier.weight(1f)) {
                label()
            }
            Row {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.White.copy(.2f)
                )
            }
        }
        HorizontalDivider(color = Color.White.copy(.1f), thickness = 1.dp)
    }
}

@Composable
fun ProfileStatistics(
    createdCalendarsSize: Int,
    receivedCalendarsSize: Int,
    friendsSize: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .padding(horizontal = 20.dp)
        ,
        horizontalArrangement = Arrangement.Center
    ) {
        ProfileStatisticsItem(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.created_),
            data = createdCalendarsSize
        )
        ProfileStatisticsItem(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.received_),
            data = receivedCalendarsSize
        )
        ProfileStatisticsItem(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.friends),
            data = friendsSize
        )
    }
}

@Composable
fun ProfileStatisticsItem(
    modifier: Modifier,
    title: String,
    data: Int
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = data.toString(), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(.5f))
    }
}

@Composable
fun ProfileHeader(
    currentUser: FirebaseUser?
) {
    if (currentUser != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentUser.photoUrl)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .border(2.dp, Purple, CircleShape)
                    .padding(4.dp)
                    .border(2.dp, DarkGrey, CircleShape)
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = currentUser.displayName.toString(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = currentUser.email.toString(), color = Color.White.copy(.3f), modifier = Modifier
                .background(
                    DarkGrey, CircleShape
                )
                .border(1.dp, Color.White.copy(.0f), CircleShape)
                .padding(horizontal = 10.dp, vertical = 2.dp), style = MaterialTheme.typography.bodyMedium)
        }
    }
}
