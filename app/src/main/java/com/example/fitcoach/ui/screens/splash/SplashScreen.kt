package com.example.fitcoach.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitcoach.R
import com.example.fitcoach.ui.theme.Orange



//Pantalla de inicio que muestra un fondo con una imagen y difuminado para darle más protagonismo al resto de componentes
// como el logo y el botón para navegar a la pantalla de iniciar sesión
@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundImage()

        GradientOverlay()

        MainContent(onNavigateToLogin) // Logo y botón

    }
}

// Función para la imagen de fondo
@Composable
private fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.fondo_inicio),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        // Filtro para aplicar a la imagen tonos grises
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
    )
}

// Función para añadir un gradiente a la imagen
@Composable
private fun GradientOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x80000000),
                        Color(0x80000000)
                    ),
                    startY = 1000f
                )
            )
    )
}

// Contenido principal de la pantalla
@Composable
private fun MainContent(onNavigateToLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Logo de la aplicación
        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = stringResource(R.string.app_logo),
            modifier = Modifier.size(300.dp).border(
                width = 2.dp,  // Grosor del borde
                color = Orange,  // Color del borde
                shape = RoundedCornerShape(16.dp)  // Bordes redondeados
            )
                // Opcional: añadir padding para que la imagen no toque el borde
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        // Botón de inicio de sesión
        LoginButton(onNavigateToLogin)


        Spacer(modifier = Modifier.weight(0.5f))
    }
}

// Función que crea el botón
@Composable
private fun LoginButton(onNavigateToLogin: () -> Unit) {
    Button(
        onClick = {
            onNavigateToLogin()
        },
        modifier = Modifier
            .width(250.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange,
            contentColor = Color.White
        )
    ) {
        Text(
            text = stringResource(R.string.start_session),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}