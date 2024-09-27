package com.example.daisy.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

enum class IconOptionUi(
    val label: String,
    val icon: ImageVector,
) {
    LOVE("Love", Icons.Default.Favorite),
    CAKE("Cake", Icons.Default.Cake);

    companion object {
        fun fromLabel(label: String): IconOptionUi? {
            return entries.find { it.label.equals(label, ignoreCase = true) }
        }
    }
}