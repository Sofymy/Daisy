package com.example.daisy.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

enum class IconOptionUi(
    val label: String,
    val icon: ImageVector,
) {
    LOVE("Love", Icons.Default.Favorite),
    CELEBRATION("Celebrate", Icons.Default.Celebration),
    GIFT("Gift", Icons.Default.CardGiftcard),
    CAKE("Cake", Icons.Default.Cake);

    companion object {
        fun fromLabel(label: String): IconOptionUi? {
            return entries.find { it.label.equals(label, ignoreCase = true) }
        }
    }
}