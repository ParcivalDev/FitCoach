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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
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

// Funciones que definen los componentes de la pantalla de inicio de sesión
// Se adaptan a la orientación de la pantalla
// LoginBackground: Fondo de la pantalla de inicio de sesión
@Composable
fun LoginBackground(isPortrait: Boolean) {
    Image(
        painter = painterResource(id = R.drawable.fondo_login),
        contentDescription = stringResource(R.string.fondo_login),
        modifier = Modifier.fillMaxSize(),
        contentScale = if (isPortrait) ContentScale.Crop else ContentScale.FillWidth
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

// Botón de ayuda que muestra un diálogo de contacto
@Composable
fun IconHelp(
    onShowContactDialog: () -> Unit, // Unit porque no devuelve nada
    isPortrait: Boolean
) {
    IconButton(
        onClick = { onShowContactDialog() },
        modifier = Modifier.padding(if (isPortrait) 24.dp else 16.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.HelpOutline,
            contentDescription = stringResource(R.string.btn_ayuda),
            tint = DarkOrange,
            modifier = Modifier.size(if (isPortrait) 36.dp else 30.dp)
        )
    }
}

// Campo de texto para el email
@Composable
fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String,
    isPortrait: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.email),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = if (isPortrait) 16.sp else 14.sp
            ),
            modifier = Modifier.align(Alignment.Start)
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = {
                Text(
                    stringResource(R.string.ej_email),
                    fontSize = if (isPortrait) 14.sp else 12.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isPortrait) 8.dp else 6.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Email,
                    contentDescription = stringResource(R.string.email),
                    tint = Color.Black
                )
            },
            isError = isError,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.DarkGray,
                disabledTextColor = Color.Gray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFC5C4C4),
                focusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else DarkOrange,
                unfocusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                cursorColor = DarkOrange,
                focusedPlaceholderColor = Color.LightGray.copy(alpha = 0.7f),
                unfocusedPlaceholderColor = Color.Gray.copy(alpha = 0.8f),
                errorContainerColor = Color.White,
                errorIndicatorColor = MaterialTheme.colorScheme.error
            )

        )
        if (isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = if (isPortrait) 12.sp else 10.sp
                ),
                modifier = Modifier.padding(
                    start = if (isPortrait) 16.dp else 12.dp,
                    top = if (isPortrait) 4.dp else 2.dp
                )
            )
        }
    }
}

// Campo de texto para la contraseña con visibilidad de la contraseña y mensaje de error
@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    isError: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier,
    isPortrait: Boolean
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.contrasenha),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = if (isPortrait) 16.sp else 14.sp
            ),
            modifier = Modifier.align(Alignment.Start)
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = {
                Text(
                    stringResource(R.string.ej_pass),
                    fontSize = if (isPortrait) 14.sp else 12.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isPortrait) 8.dp else 6.dp),
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = { // Icono de la contraseña
                Icon(
                    imageVector = Icons.Rounded.Password,
                    contentDescription = stringResource(R.string.icon_pass),
                    tint = Color.Black
                )
            },
            trailingIcon = { // Icono de visibilidad de la contraseña
                IconButton(onClick = onPasswordVisibilityChange) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = if (isPasswordVisible) stringResource(R.string.ocultar_pass) else stringResource(
                            R.string.mostrar_pass
                        ),
                        tint = Color.Black
                    )
                }
            },
            isError = isError,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.DarkGray,
                disabledTextColor = Color.Gray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFC5C4C4),
                focusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else DarkOrange,
                unfocusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                cursorColor = DarkOrange,
                focusedPlaceholderColor = Color.LightGray.copy(alpha = 0.7f),
                unfocusedPlaceholderColor = Color.Gray.copy(alpha = 0.8f),
                errorContainerColor = Color.White,
                errorIndicatorColor = MaterialTheme.colorScheme.error
            )

        )
        if (isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = if (isPortrait) 12.sp else 10.sp
                ),
                modifier = Modifier.padding(
                    start = if (isPortrait) 16.dp else 12.dp,
                    top = if (isPortrait) 4.dp else 2.dp
                )
            )
        }
    }
}

// Opciones de inicio de sesión: Recuérdame y Recuperar contraseña
@Composable
fun LoginOptions(
    rememberMe: Boolean,
    onRememberMeChange: (Boolean) -> Unit,
    onPasswordRecoveryClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPortrait: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = if (isPortrait) 8.dp else 6.dp),
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
                text = stringResource(R.string.recuerdame),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp
                ),
                color = Color.DarkGray
            )
        }

        VerticalDivider(
            modifier = Modifier
                .height(if (isPortrait) 24.dp else 20.dp)
                .width(1.dp)
                .background(Color.LightGray)
        )


        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            TextButton(
                onClick = onPasswordRecoveryClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(R.string.titulo_recu_pass),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                    textAlign = TextAlign.End,
                    color = LightBlue
                )
            }
        }
    }
}

// Botón de inicio de sesión
@Composable
fun LoginButton(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean,
    isPortrait: Boolean
) {
    Button(
        onClick = { onNavigateToHome() },
        modifier = modifier
            .fillMaxWidth(if (isPortrait) 0.6f else 0.4f)
            .height(if (isPortrait) 56.dp else 48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange,
            disabledContainerColor = Orange.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(if (isPortrait) 28.dp else 24.dp),
        enabled = enabled && !isLoading // Deshabilita el botón si está cargando
    ) {
        if (isLoading) { // Muestra un indicador de carga si isLoading es verdadero
            CircularProgressIndicator(
                modifier = Modifier.size(if (isPortrait) 24.dp else 20.dp),
                color = Color.White
            )
        } else { // Muestra el texto del botón si isLoading es falso
            Text(
                stringResource(R.string.iniciar_sesion),
                color = Color.White,
                fontSize = if (isPortrait) 16.sp else 14.sp
            )
        }
    }
}