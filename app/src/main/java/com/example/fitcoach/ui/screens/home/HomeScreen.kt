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
import androidx.compose.material.icons.rounded.Newspaper
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.fitcoach.R
import com.example.fitcoach.ui.navigation.Screen
import com.example.fitcoach.ui.theme.Orange


val BackgroundLight = Color(0xFFF5F5F5)
val BackgroundDark = Color(0xFF030B1B)
val DarkBlueLight = Color(0xFF484f69)
val DarkBlueDark = Color(0xFF1A2234)
val CardLight = Color(0xFF9fabce)
val CardDark = Color(0xFF1E2A4A)
val AccentOrange = Color(0xFFFF6B00)

/*@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)*/
@Composable
fun HomeScreen(navController: NavHostController) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight
    val textColor = if (isDarkTheme) Color.White else Color.Black


    Scaffold(
        topBar = { TopAppBar(isDarkTheme) },
        //bottomBar = { BottomNavBar(isDarkTheme, navController) },
        bottomBar = { CommonBottomBar(navController, isDarkTheme) },
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
            OtherCategories()
            LatestNews(cardColor)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(isDarkTheme: Boolean) {
    val backgroundColor = if (isDarkTheme) DarkBlueDark else DarkBlueLight
    val contentColor = Color.White
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
            /*.background(
                Color(0x00132A4A),
                CircleShape
            )  // Fondo circular del color de las tarjetas*/

            // .background(Color(0xFFD1C4E9), CircleShape) //0xFFD1C4E9
            //.padding(0.dp),
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
fun OtherCategories() {
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
                    .background(Color.Black.copy(alpha = 0.1f))

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
        "Blog" -> R.drawable.blog_img
        else -> R.drawable.ic_image_not_found
    }
}

@Composable
fun LatestNews(cardColor: Color) {
    val blog = "Blog"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = getCategoryBackgroundImage(blog)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Overlay para mejorar la legibilidad del texto
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))

            )

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Newspaper,
                    contentDescription = "Icono de blog",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(30.dp)
                )
                // Texto de la categoría
                Text(
                    text = blog,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
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
fun BottomNavBar(isDarkTheme: Boolean, navController: NavHostController) {
    val backgroundColor = if (isDarkTheme) DarkBlueDark else DarkBlueLight
    val contentColor = Color.White

    data class NavItem(val icon: ImageVector, val label: String, val route: String)

    val items = listOf(
        NavItem(Icons.Rounded.Timer, "Timer", Screen.Timer.route),
        NavItem(Icons.Rounded.Home, "Home", Screen.Home.route),
        NavItem(
            Icons.Rounded.DateRange,
            "Calendar",
            "calendar"
        ) // Asume que tienes una ruta para Calendar
    )

    /*NavigationBar(containerColor = backgroundColor) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = navController.currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentOrange,
                    unselectedIconColor = contentColor.copy(alpha = 0.7f),
                    selectedTextColor = AccentOrange,
                    unselectedTextColor = contentColor.copy(alpha = 0.7f),
                    indicatorColor = contentColor.copy(alpha = 0.1f)
                )
            )
        }
    }*/
}


@Composable
fun CommonBottomBar(navController: NavHostController, isDarkTheme: Boolean) {
    val backgroundColor = if (isDarkTheme) DarkBlueDark else DarkBlueLight
    val contentColor = Color.White

    data class NavItem(val icon: ImageVector, val label: String, val route: String)

    val items = listOf(
        NavItem(Icons.Rounded.Timer, "Timer", Screen.Timer.route),
        NavItem(Icons.Rounded.Home, "Home", Screen.Home.route),
        NavItem(
            Icons.Rounded.DateRange,
            "Calendar",
            "calendar"
        ) // Asume que tienes una ruta para Calendar
    )

    NavigationBar(containerColor = backgroundColor) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = navController.currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
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