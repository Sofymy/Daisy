package com.example.daisy.ui.common.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun PrimaryTextFieldPreview(){
    PrimaryTextField(
        value = "Preview value",
        onValueChange = {  },
        label = "Preview label",
        icon = Icons.Default.AlternateEmail,
    )
}

@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String = "",
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector
){
    Column(
        Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Text(label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            leadingIcon = { Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground.copy(0.2f)) },
            placeholder = { Text(placeholderText, color = MaterialTheme.colorScheme.onBackground.copy(0.2f)) },
            value = value,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(10),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(0.1f),
                unfocusedContainerColor = Color.White.copy(0.1f),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onValueChange
        )
    }

}