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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHome(isDarkTheme: Boolean, onProfileClick: () -> Unit) {
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
            IconButton(onClick = onProfileClick) {
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
            .padding(vertical = 16.dp)
            .clickable { onBlogClick() },
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
        NavItem(Icons.Rounded.Timer, "Temporizador", Screen.Timer.route),
        NavItem(Icons.Rounded.Home, "Inicio", Screen.Home.route),
        NavItem(
            Icons.Rounded.DateRange,
            "Calendario",
            Screen.Calendar.route
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

@Composable
fun DrawerContent(
    userName: String,
    backgroundColor: Color,
    drawerState: DrawerState,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSocialClick: (String) -> Unit,
    onSupportClick: (String) -> Unit,
    onLogoutClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    fun handleClick(action: () -> Unit) {
        scope.launch {
            drawerState.close()
            action()
        }
    }
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = backgroundColor
    ) {
        // Header con foto de perfil y nombre
        DrawerHeader(userName)

        // Sección Usuario
        DrawerSection(title = "Usuario") {
            DrawerItem(
                icon = Icons.Rounded.Person,
                label = "Mi perfil",
                onClick = {
                    handleClick(onProfileClick)

                }
            )
            DrawerItem(
                icon = Icons.Rounded.Settings,
                label = "Ajustes",
                onClick = {
                    handleClick(onSettingsClick)
                }
            )
        }

        // Sección Social
        DrawerSection(title = "Social") {
            DrawerItem(
                icon = R.drawable.instagram_icon,
                label = "Instagram",
                onClick = {
                    handleClick { onSocialClick("instagram") }
                }
            )
            DrawerItem(
                icon = R.drawable.youtube_icon,
                label = "YouTube",
                onClick = {
                    handleClick { onSocialClick("youtube") }
                }
            )
            DrawerItem(
                icon = R.drawable.spotify_icon,
                label = "Spotify",
                onClick = {
                    handleClick { onSocialClick("spotify") }
                }
            )
        }

        // Sección Soporte
        DrawerSection(title = "Soporte") {
            DrawerItem(
                icon = Icons.AutoMirrored.Rounded.Help,
                label = "Ayuda",
                onClick = {
                    handleClick { onSupportClick("help") }
                }
            )
            DrawerItem(
                icon = Icons.Rounded.Email,
                label = "Contacto",
                onClick = {
                    handleClick { onSupportClick("contact") }
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(Modifier.fillMaxWidth())
        // Botón de Cerrar Sesión
        DrawerItem(
            icon = Icons.AutoMirrored.Rounded.Logout,
            label = "Cerrar Sesión",
            onClick = {
                handleClick(onLogoutClick)
            }
        )
    }
}

@Composable
private fun DrawerHeader(userName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart)
        ) {
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

@Composable
private fun DrawerSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 28.dp, top = 16.dp, bottom = 8.dp)
        )
        content()
    }
}

@Composable
private fun DrawerItem(
    icon: Any,
    label: String,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            when (icon) {
                is ImageVector -> Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(24.dp)
                )

                is Int -> Icon(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        label = { Text(label) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}