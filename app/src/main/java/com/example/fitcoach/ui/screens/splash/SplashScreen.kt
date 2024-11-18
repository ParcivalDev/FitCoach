package com.example.fitcoach.ui.screens.splash

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitcoach.R
import com.example.fitcoach.ui.theme.Orange

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen {}
}


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
        contentDescription = stringResource(R.string.fondo_splash),
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
    // Detectar la orientación actual
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = if (isPortrait) 24.dp else 80.dp,
                vertical = if (isPortrait) 24.dp else 32.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Logo adaptable según orientación
        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = stringResource(R.string.app_logo),
            modifier = Modifier.size(if (isPortrait) 250.dp else 200.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón de inicio de sesión
        LoginButton(onNavigateToLogin, isPortrait)


        Spacer(modifier = Modifier.weight(0.2f))
    }
}

// Función que crea el botón
@Composable
private fun LoginButton(onNavigateToLogin: () -> Unit, isPortrait: Boolean) {
    Button(
        onClick = { onNavigateToLogin() },
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(if (isPortrait) 56.dp else 48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        Text(
            text = stringResource(R.string.start_session),
            fontWeight = FontWeight.Bold,
            fontSize = if (isPortrait) 18.sp else 16.sp
        )
    }
}