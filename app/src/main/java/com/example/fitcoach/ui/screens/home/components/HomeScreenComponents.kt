package com.example.fitcoach.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitcoach.R
import com.example.fitcoach.ui.navigation.Screen
import com.example.fitcoach.ui.screens.home.model.BlogPost
import com.example.fitcoach.ui.screens.home.model.Category
import com.example.fitcoach.ui.screens.home.model.ExerciseCategory
import com.example.fitcoach.ui.theme.AccentOrange
import com.example.fitcoach.ui.theme.CardDark
import com.example.fitcoach.ui.theme.DarkBlueDark
import com.example.fitcoach.ui.theme.DarkBlueLight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHome(isDarkTheme: Boolean) {
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
fun ExerciseLibrary(
    exercises: List<ExerciseCategory>,
    textColor: Color,
    onExerciseClick: (ExerciseCategory) -> Unit
) {
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
            items(exercises) { exercise ->
                ExerciseItem(
                    exercise = exercise,
                    textColor = textColor,
                    onExerciseClick = { onExerciseClick(exercise) }
                )
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: ExerciseCategory,
    textColor: Color,
    onExerciseClick: (ExerciseCategory) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onExerciseClick(exercise) }
    ) {
        Image(
            painter = painterResource(id = exercise.imageResource),
            contentDescription = "Imagen de ${exercise.name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(exercise.name, style = MaterialTheme.typography.bodyMedium, color = textColor)
    }
}


@Composable
fun OtherCategories(categories: List<Category>, onCategoryClick: (Category) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryItem(categories[0], { onCategoryClick(categories[0]) }, Modifier.weight(1f))
            CategoryItem(categories[1], { onCategoryClick(categories[1]) }, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryItem(categories[2], { onCategoryClick(categories[2]) }, Modifier.weight(1f))
            CategoryItem(categories[3], { onCategoryClick(categories[3]) }, Modifier.weight(1f))
        }
    }
}

@Composable
fun CategoryItem(category: Category, onCategoryClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clickable { onCategoryClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = category.imageResource),
                contentDescription = "Imagen de ${category.name}",
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
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            )
        }
    }
}


@Composable
fun LatestNews(blogPost: BlogPost, cardColor: Color, onBlogClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 16.dp).clickable { onBlogClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = blogPost.imageResource),
                contentDescription = "Imagen de blog",
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
                    text = blogPost.title,
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
fun Greeting(userName: String, textColor: Color) {
    Text(
        "Hola, $userName!",
        style = MaterialTheme.typography.headlineSmall,
        color = textColor,
    )
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
        )
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