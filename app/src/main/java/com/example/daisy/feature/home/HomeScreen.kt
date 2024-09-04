package com.example.daisy.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    onNavigateToNewCalendar: () -> Unit,
    onNavigateToCreatedCalendars: () -> Unit
) {
    Column {
        Text(text = "Home")
        Button(onClick = { onNavigateToNewCalendar() }) {

        }
        Button(onClick = { onNavigateToCreatedCalendars() }) {

        }
    }
}