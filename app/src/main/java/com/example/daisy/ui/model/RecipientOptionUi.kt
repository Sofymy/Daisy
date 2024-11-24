package com.example.daisy.ui.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.daisy.R

enum class RecipientOption(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    @StringRes val descriptionRes: Int
) {
    CODE(
        R.string.recipient_option_code_label,
        Icons.Default.Numbers,
        R.string.recipient_option_code_description
    ),
    EMAIL(
        R.string.recipient_option_email_label,
        Icons.Default.Email,
        R.string.recipient_option_email_description
    )
}