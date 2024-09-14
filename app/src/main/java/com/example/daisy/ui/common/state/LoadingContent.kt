package com.example.daisy.ui.common.state

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.daisy.ui.theme.Blue
import com.example.daisy.ui.theme.Purple

@Composable
fun LoadingContent() {
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            Modifier.size(70.dp),
            color = Purple
        )
        Icon(
            Icons.Filled.Rocket, null, Modifier.size(40.dp), tint = Purple
        )
    }
}