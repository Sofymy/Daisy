package com.example.daisy.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.ui.graphics.vector.ImageVector

enum class RecipientOption(
    val label: String,
    val icon: ImageVector,
    val description: String
) {
    CODE("By code", Icons.Default.Numbers, "We'll automatically generate a six-digit code for you, which you can share with one or more recipients in several ways."),
    EMAIL("Via email", Icons.Default.Email, "Enter the email address of a contact and your calendar will be assigned to them directly."),
}