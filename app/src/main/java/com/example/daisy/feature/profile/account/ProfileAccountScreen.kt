package com.example.daisy.feature.profile.account

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.daisy.R
import com.example.daisy.feature.profile.ProfileItem
import com.example.daisy.ui.common.elements.PrimaryTextField
import com.example.daisy.ui.common.elements.SecondaryButton
import com.example.daisy.ui.common.elements.TertiaryButton
import com.example.daisy.ui.common.state.HandleLifecycleEvents
import com.example.daisy.ui.common.state.LoadingContent
import com.example.daisy.ui.theme.DarkGrey
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.util.UiEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun ProfileAccountScreen(
    onNavigateToProfile: () -> Unit
){
    ProfileAccountScreenContent(
        onNavigateToProfile = onNavigateToProfile
    )
}

@Composable
fun ProfileAccountScreenContent(
    viewModel: ProfileAccountViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleLifecycleEvents(
        onResume = {
            viewModel.onEvent(ProfileAccountUserEvent.GetCurrentUser)
        }
    )

    HandleUiEvents(
        uiEvent = viewModel.uiEvent,
        onNavigateToSignIn = onNavigateToProfile
    )

    when {
        state.isLoading -> LoadingContent()
        else -> {
            LazyColumn(
                Modifier.fillMaxSize()
            ) {
                item {
                    ProfileAccountHeader(
                        state = state,
                        onUriChange = {
                            viewModel.onEvent(ProfileAccountUserEvent.PhotoUriChanged(it))
                        }
                    )
                }
                item {
                    ProfileAccountForm(
                        state = state,
                        onNameValueChange = {
                            viewModel.onEvent(ProfileAccountUserEvent.NameChanged(it))
                        },
                        onClickSave = {
                            viewModel.onEvent(ProfileAccountUserEvent.SaveChanges)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HandleUiEvents(
    uiEvent: Flow<UiEvent>,
    onNavigateToSignIn: () -> Unit
) {
    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    onNavigateToSignIn()
                }
                is UiEvent.Error -> {
                }
            }
        }
    }
}

@Composable
fun ProfileAccountHeader(
    onUriChange: (Uri) -> Unit,
    state: ProfileAccountUiState
) {
    var imageUri by remember {
        mutableStateOf(state.newPhotoUri)
    }

    val contentResolver = LocalContext.current.contentResolver
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            val flags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri, flags)
            onUriChange(uri)
        }
    }

    if (state.currentUser != null) {
        Box(
            modifier = Modifier
                .padding(top = 15.dp, bottom = 30.dp)
                .fillMaxWidth()
            ,
            contentAlignment = Alignment.Center
        ){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
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
            Box(modifier = Modifier
                .offset(x = 50.dp, y = 32.dp)
                .size(35.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(3.dp)
                .clickable {
                    launcher.launch("image/*")
                }
                ,
                contentAlignment = Alignment.Center
            ){
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = null, tint = Color.Black)
            }
        }
    }
}

@Composable
fun ProfileAccountForm(
    state: ProfileAccountUiState,
    onNameValueChange: (String) -> Unit,
    onClickSave: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End,
    ) {
        ProfileItem(
            title = stringResource(R.string.account_details),
        ) {
            Column(
                Modifier.background(MediumGrey)
            ) {
                PrimaryTextField(
                    enabled = false,
                    icon = Icons.Default.AlternateEmail,
                    value = state.currentUser?.email ?: "",
                    keyboardType = KeyboardType.Email,
                    onValueChange = {  },
                    label = stringResource(id = R.string.email)
                )
                PrimaryTextField(
                    enabled = false,
                    icon = Icons.Default.Password,
                    value = "●●●●●●●●●●●",
                    onValueChange = {  },
                    label = stringResource(id = R.string.password)
                )
                Row(
                    Modifier
                        .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                        .fillMaxWidth(.6f)
                        .align(Alignment.End), horizontalArrangement = Arrangement.End) {
                    TertiaryButton(onClick = { /*TODO*/ }) {
                        Text(text = stringResource(R.string.change_password))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color.White.copy(.2f))
            }
        }


        ProfileItem(
            title = stringResource(R.string.personal_details),
        ) {

            Column(
                Modifier.background(MediumGrey)
            ) {
                PrimaryTextField(
                    value = state.newName,
                    onValueChange = { onNameValueChange(it) },
                    placeholderText = stringResource(R.string.name_),
                    label = stringResource(id = R.string.name),
                    icon = Icons.Outlined.AccountCircle
                )
                Row(
                    Modifier
                        .padding(start = 20.dp, top = 20.dp, end = 30.dp)
                        .align(Alignment.End), horizontalArrangement = Arrangement.End) {
                    SecondaryButton(onClick = onClickSave) {
                        Text(text = stringResource(R.string.save_))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color.White.copy(.2f))
            }

        }
    }
    Spacer(modifier = Modifier.height(40.dp))

}