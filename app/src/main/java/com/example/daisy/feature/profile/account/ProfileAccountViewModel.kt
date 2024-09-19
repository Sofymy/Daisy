package com.example.daisy.feature.profile.account

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daisy.domain.model.toUi
import com.example.daisy.domain.usecases.auth.AuthUseCases
import com.example.daisy.domain.usecases.calendar.CalendarUseCases
import com.example.daisy.feature.auth.register.RegisterUserEvent
import com.example.daisy.feature.home.HomeUserEvent
import com.example.daisy.ui.model.CalendarUi
import com.example.daisy.ui.util.UiEvent
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileAccountUiState(
    val isLoading: Boolean = true,
    val currentUser: FirebaseUser? = null,
    val newName: String = "",
    val newPhotoUri: Uri? = Uri.EMPTY
)

sealed class ProfileAccountUserEvent {
    data object GetCurrentUser : ProfileAccountUserEvent()
    data class NameChanged(val name: String): ProfileAccountUserEvent()
    data class PhotoUriChanged(val photoUri: Uri): ProfileAccountUserEvent()
    data object SaveChanges : ProfileAccountUserEvent()
}

@HiltViewModel
class ProfileAccountViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileAccountUiState())
    val state = _state

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: ProfileAccountUserEvent) {
        when(event){
            ProfileAccountUserEvent.GetCurrentUser -> {
                getCurrentUser()
            }

            is ProfileAccountUserEvent.NameChanged -> {
                val newName = event.name.trim()
                _state.update { it.copy(newName = newName) }
            }

            is ProfileAccountUserEvent.PhotoUriChanged -> {
                val newPhotoUri = event.photoUri
                _state.update { it.copy(newPhotoUri = newPhotoUri) }
                savePhotoUri()
            }

            ProfileAccountUserEvent.SaveChanges -> {
                saveChanges()
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val result = authUseCases.getCurrentUserUseCase()
                _state.update { it.copy(currentUser = result) }
                _state.update { it.copy(isLoading = false) }
                _state.update { it.copy(newName = result?.displayName ?: "") }
                _state.update { it.copy(newPhotoUri = result?.photoUrl) }
            } catch (_: Exception) {
            }
        }
    }

    private fun savePhotoUri() = viewModelScope.launch {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                state.value.newPhotoUri?.let { authUseCases.changePhotoUriUseCase(it) }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }

    private fun saveChanges() = viewModelScope.launch {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true) }
                val result = authUseCases.changeNameUseCase(state.value.newName)
                if (result.isSuccess) _uiEvent.send(UiEvent.Success)
                else _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(e.message.toString()))
            }
        }
    }
}