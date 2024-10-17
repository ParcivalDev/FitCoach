package com.example.fitcoach.ui.screens.login

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.HelpCenter
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material.icons.sharp.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Vertices
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitcoach.R
import com.example.fitcoach.ui.theme.DarkOrange
import com.example.fitcoach.ui.theme.FitCoachTheme
import com.example.fitcoach.ui.theme.Gray
import com.example.fitcoach.ui.theme.LightBlue
import com.example.fitcoach.ui.theme.Orange

@Preview(showBackground = true)
@Composable
fun LoginScreen(/*navController: NavController*/) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


        //       Spacer(modifier = Modifier.weight(1f))

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
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )


        // Icono de ayuda
        IconButton(
            onClick = {
                /*TODO: Implementar acción de ayuda*/
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.HelpOutline,
                contentDescription = "Ayuda",
                tint = DarkOrange,
                modifier = Modifier.size(36.dp)
            )
        }



        Card(
            modifier = Modifier
                .fillMaxWidth().align(Alignment.Center)
                .padding(horizontal = 16.dp),
            //colors = CardDefaults.cardColors(containerColor = Color.White)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
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

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Contraseña",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("*******") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Password,
                            contentDescription = "Contraseña"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
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
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = true/*recuerdame*/,
                            onCheckedChange = { /*recuerdame = it*/ },
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
                            onClick = { /*TODO*/ },
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

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { /* Implementar lógica de login */ },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    shape = RoundedCornerShape(25.dp) // Más redondeado para parecerse a la imagen
                ) {
                    Text("Iniciar sesión", color = Color.White, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


