package com.example.fitcoach.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.fitcoach.R
import com.example.fitcoach.ui.theme.Orange


val BackgroundLight = Color(0xFFF5F5F5)
val BackgroundDark = Color(0xFF030B1B)
val DarkBlueLight = Color(0xFF9fabce)
val DarkBlueDark = Color(0xFF1A2234)
val CardLight = Color(0xFF9fabce)
val CardDark = Color(0xFF1E2A4A)
val AccentOrange = Color(0xFFFF6B00)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreen() {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight
    val textColor = if (isDarkTheme) Color.White else Color.Black


    Scaffold(
        topBar = { TopAppBar(isDarkTheme) },
        bottomBar = { BottomNavBar(isDarkTheme) },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Greeting(textColor)
            ExerciseLibrary(textColor)
            OtherCategories(textColor)
            LatestNews(cardColor, textColor)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(isDarkTheme:Boolean) {
    val backgroundColor = if (isDarkTheme) DarkBlueDark else DarkBlueLight
    val contentColor = if (isDarkTheme) Color.White else Color.Black
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "SM Logo",
                    modifier = Modifier.size(50.dp)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Rounded.AccountCircle, contentDescription = "Perfil")
            }
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Rounded.Notifications, contentDescription = "Notificaciones")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor
        )
    )
}

@Composable
fun ExerciseLibrary(textColor: Color) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(
                "Biblioteca de ejercicios",
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = "Ir a biblioteca",
                tint = textColor
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                listOf(
                    "Pectoral", "Espalda", "Trapecio", "Deltoides", "Tríceps",
                    "Bíceps", "Antebrazo", "Abdomen", "Glúteo",
                    "Cuádriceps", "Isquios", "Gemelos", "Aductores"
                )
            ) { muscle ->
                ExerciseItem(muscle, textColor)
            }
        }
    }
}

@Composable
fun ExerciseItem(muscle: String, textColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = getExerciseImageResource(muscle)),
            contentDescription = "Imagen de $muscle",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)  // Esto redondeará la imagen
                .background(
                    Color(0x00132A4A),
                    CircleShape
                )  // Fondo circular del color de las tarjetas

                // .background(Color(0xFFD1C4E9), CircleShape) //0xFFD1C4E9
                .padding(0.dp),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(muscle, style = MaterialTheme.typography.bodyMedium, color = textColor)
    }
}

//<a href="https://www.freepik.es/icono/atras_14228744">Icono de cube29</a>
//HAY QUE MENCIONAR TODAS LAS IMÁGENES
fun getExerciseImageResource(muscle: String): Int {
    val normalizedMuscle = muscle.lowercase()
        .replace("á", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u")
        .replace(" ", "_")

    return try {
        val resourceName = "${normalizedMuscle}_img"
        val resourceId = R.drawable::class.java.getField(resourceName).getInt(null)
        if (resourceId != 0) resourceId else R.drawable.ic_image_not_found
    } catch (e: Exception) {
        // Log.e("ExerciseImage", "Image not found for muscle: $muscle", e)
        R.drawable.ic_image_not_found
    }
}


@Composable
fun OtherCategories(cardColor: Color) {
    val categories = listOf("Entrenamiento", "Academia", "Progreso", "Tienda")
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryItem(categories[0], Modifier.weight(1f))
            CategoryItem(categories[1], Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryItem(categories[2], Modifier.weight(1f))
            CategoryItem(categories[3], Modifier.weight(1f))
        }
    }
}

@Composable
fun CategoryItem(category: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = getCategoryBackgroundImage(category)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Overlay para mejorar la legibilidad del texto
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0f))

            )

            // Texto de la categoría
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            )
        }
    }
}

fun getCategoryBackgroundImage(category: String): Int {
    return when (category) {
        "Entrenamiento" -> R.drawable.entrenamiento_img
        "Progreso" -> R.drawable.progreso_img
        "Academia" -> R.drawable.academia_img
        "Tienda" -> R.drawable.tienda_img
        else -> R.drawable.ic_image_not_found
    }
}

@Composable
fun LatestNews(cardColor: Color, textColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Últimas noticias",
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )

        }
    }
}

@Composable
fun Greeting(textColor: Color) {
    Text(
        "Hola, Usuario!",
        style = MaterialTheme.typography.headlineSmall,
        color = textColor,
    )
}

@Composable
fun BottomNavBar(isDarkTheme: Boolean) {
    val backgroundColor = if (isDarkTheme) DarkBlueDark else DarkBlueLight
    val contentColor = if (isDarkTheme) Color.White else Color.Black

    NavigationBar(
        containerColor = backgroundColor
    ) {
        val items = listOf(
            Triple(Icons.Rounded.Timer, "Timer", false),
            Triple(Icons.Rounded.Home, "Home", true),
            Triple(Icons.Rounded.DateRange, "Calendar", false)
        )

        items.forEach { (icon, label, selected) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                selected = selected,
                onClick = { /* TODO */ },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentOrange,
                    unselectedIconColor = contentColor.copy(alpha = 0.7f),
                    selectedTextColor = AccentOrange,
                    unselectedTextColor = contentColor.copy(alpha = 0.7f),
                    indicatorColor = contentColor.copy(alpha = 0.1f)
                )
            )
        }
    }
}