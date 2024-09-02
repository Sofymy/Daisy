package com.example.daisy.feature.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    onNavigateToCreateCalendar: () -> Unit
) {
    Text(text = "Home")
    Button(onClick = { onNavigateToCreateCalendar() }) {

    }
}