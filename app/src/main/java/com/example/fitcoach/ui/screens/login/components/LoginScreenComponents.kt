package com.example.fitcoach.ui.screens.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitcoach.R
import com.example.fitcoach.ui.theme.DarkOrange
import com.example.fitcoach.ui.theme.Gray
import com.example.fitcoach.ui.theme.LightBlue
import com.example.fitcoach.ui.theme.Orange

@Composable
fun LoginBackground() {
    Image(
        painter = painterResource(id = R.drawable.fondo_login),
        contentDescription = "Fondo Login",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.6f),
                        Color.Black.copy(alpha = 0.6f)
                    )
                )
            )
    )
}

@Composable
fun IconHelp(onShowContactDialog: () -> Unit) {
    IconButton(
        onClick = { onShowContactDialog() },
        modifier = Modifier.padding(24.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.HelpOutline,
            contentDescription = "Ayuda",
            tint = DarkOrange,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Email",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("usuario@gmail.com") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Email,
                    contentDescription = "Email"
                )
            },
            //isError = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.DarkGray,
                disabledTextColor = Color.Gray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFC5C4C4),
                focusedIndicatorColor = DarkOrange,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = DarkOrange,
                focusedPlaceholderColor = Color.LightGray.copy(alpha = 0.7f),
                unfocusedPlaceholderColor = Color.Gray.copy(alpha = 0.8f)
            )

        )
    }
}

@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Contraseña",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = { Text("*******") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Password,
                    contentDescription = "Contraseña"
                )
            },
            trailingIcon = {
                IconButton(onClick = onPasswordVisibilityChange) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            //isError = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.DarkGray,
                disabledTextColor = Color.Gray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFC5C4C4),
                focusedIndicatorColor = DarkOrange,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = DarkOrange,
                focusedPlaceholderColor = Color.LightGray.copy(alpha = 0.7f),
                unfocusedPlaceholderColor = Color.Gray.copy(alpha = 0.8f)
            )

        )
    }
}

@Composable
fun LoginOptions(
    rememberMe: Boolean,
    onRememberMeChange: (Boolean) -> Unit,
    onPasswordRecoveryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = onRememberMeChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Orange,
                    uncheckedColor = Gray,
                    checkmarkColor = Color.White
                )
            )
            Text(
                text = "Recuérdame",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }

        VerticalDivider(
            modifier = Modifier
                .height(24.dp)
                .width(1.dp)
                .background(Color.LightGray)
        )


        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            TextButton(
                onClick = onPasswordRecoveryClick,
                contentPadding = PaddingValues(0.dp),
                /*colors = ButtonDefaults.textButtonColors(contentColor = Orange)*/

            ) {
                Text(
                    text = "Recuperar contraseña",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    color = LightBlue
                )
            }
        }
    }
}

@Composable
fun LoginButton(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onNavigateToHome() },
        modifier = modifier
            .fillMaxWidth(0.6f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Orange),
        shape = RoundedCornerShape(25.dp) // Más redondeado para parecerse a la imagen
    ) {
        Text("Iniciar sesión", color = Color.White, fontSize = 16.sp)
    }
}