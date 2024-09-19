package com.example.daisy.domain.usecases.auth

import android.net.Uri
import com.example.daisy.data.datasource.auth.AuthenticationService
import javax.inject.Inject

class ChangePhotoUriUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend operator fun invoke(uri: Uri) =
        authenticationService.changePhotoUri(uri)
}