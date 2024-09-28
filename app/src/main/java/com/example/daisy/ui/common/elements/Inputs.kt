package com.example.daisy.ui.common.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    enabled: Boolean = true,
    value: String,
    onValueChange: (String) -> Unit,
    onImeAction: (() -> Unit)? = null,
    placeholderText: String = "",
    label: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector
){
    val showPassword = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current


    Column(
        Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        if (label != null) {
            Text(label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
        }
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            enabled = enabled,

            singleLine = true,
            leadingIcon = { Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground.copy(0.2f)) },
            trailingIcon = {
                val trailingIcon = if (showPassword.value) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                if(keyboardType == KeyboardType.Password) {
                    IconButton(onClick = { showPassword.value = !showPassword.value }) {
                        Icon(
                            trailingIcon,
                            contentDescription = "Visibility",
                            tint = Color.White
                        )
                    }
                }
            },
            placeholder = { Text(placeholderText, color = MaterialTheme.colorScheme.onBackground.copy(0.2f)) },
            value = value,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onImeAction?.invoke()
                    focusManager.clearFocus()
                }
            ),
            visualTransformation = if (keyboardType == KeyboardType.Password && !showPassword.value ) PasswordVisualTransformation() else VisualTransformation.None,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(0.1f),
                unfocusedContainerColor = Color.White.copy(0.1f),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.White.copy(0.05f),
                disabledTextColor = Color.White.copy(0.2f),
                disabledLeadingIconColor = Color.White.copy(0.2f),
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onValueChange
        )
    }

}