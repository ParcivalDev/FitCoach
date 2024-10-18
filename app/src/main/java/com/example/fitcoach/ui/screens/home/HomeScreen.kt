package com.example.fitcoach.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.fitcoach.R
import com.example.fitcoach.ui.theme.Orange

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = { TopAppBar() },
        bottomBar = { BottomNavBar() },
        containerColor = Color(0xFFF2F2F2) // Light purple background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Greeting()
            ExerciseLibrary()
            OtherCategories()
            LatestNews()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
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
            containerColor = Color(0xFFF3E5F5), // Light purple
            titleContentColor = Color.Black,
        )
    )
}

@Composable
fun ExerciseLibrary() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text("Biblioteca de ejercicios", style = MaterialTheme.typography.titleMedium)
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "Ir a biblioteca")
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
                ExerciseItem(muscle)
            }
        }
    }
}

@Composable
fun ExerciseItem(muscle: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = getExerciseImageResource(muscle)),
            contentDescription = "Imagen de $muscle",
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFFFCCBC), CircleShape) //0xFFD1C4E9
                // o .clip(CircleShape)
                .padding(8.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(muscle, style = MaterialTheme.typography.bodyMedium)
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
fun OtherCategories() {
    val categories = listOf("Entrenamiento", "Progreso", "Academia", "Tienda")
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
        shape = RoundedCornerShape(16.dp), //¿?¿?¿?¿??
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
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
fun LatestNews() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Últimas noticias", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            // Aquí puedes añadir el contenido de las últimas noticias
        }
    }
}

@Composable
fun Greeting() {
    Text(
        "Hola, Usuario!",
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Timer, contentDescription = "Timer") },
            label = { Text("Timer") },
            selected = false,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
            label = { Text("Calendar") },
            selected = false,
            onClick = { /* TODO */ }
        )
    }
}