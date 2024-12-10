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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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


// Barra superior de la pantalla de inicio
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHome(
    isDarkTheme: Boolean,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    isPortrait: Boolean
) {
    val backgroundColor = if (isDarkTheme) DarkBlueDark else DarkBlueLight
    val contentColor = Color.White
    TopAppBar(
        title = {
            Box( // Centra el logo en la barra superior
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = stringResource(R.string.sm_logo),
                    modifier = Modifier.size(if (isPortrait) 50.dp else 40.dp)
                )
            }
        },
        navigationIcon = { // navigationIcon es el icono de perfil en la barra superior izquierda
            IconButton(onClick = onProfileClick) {
                Icon(
                    Icons.Rounded.AccountCircle,
                    contentDescription = stringResource(R.string.perfil)
                )
            }
        },
        actions = { // actions son los iconos de notificaciones en la barra superior derecha
            IconButton(onClick = { onNotificationClick() }) {
                Icon(
                    Icons.Rounded.Notifications,
                    contentDescription = stringResource(R.string.notificaciones)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor
        )
    )
}

// Biblioteca de ejercicios
// Muestra una lista de ejercicios con sus imágenes y nombres
// También muestra un botón para ver la biblioteca completa
@Composable
fun ExerciseLibrary(
    exercises: List<ExerciseCategory>,
    textColor: Color,
    onExerciseClick: (ExerciseCategory) -> Unit,
    onSeeAllClick: () -> Unit,
    isPortrait: Boolean
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.biblioteca_de_ejercicios),
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
            TextButton(onClick = onSeeAllClick) { // Botón para ver la biblioteca completa
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = stringResource(R.string.ver_biblioteca_completa),
                    tint = textColor
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow( // Muestra los ejercicios en una fila horizontal con scroll
            horizontalArrangement = Arrangement.spacedBy(if (isPortrait) 8.dp else 4.dp),
            modifier = Modifier.padding(vertical = if (isPortrait) 8.dp else 4.dp)
        ) {
            items(exercises) { exercise ->
                ExerciseItem(
                    exercise = exercise,
                    textColor = textColor,
                    onExerciseClick = { onExerciseClick(exercise) },
                    isPortrait = isPortrait
                )
            }
        }
    }
}

// Muestra un ejercicio con su imagen y nombre
@Composable
fun ExerciseItem(
    exercise: ExerciseCategory, // Ejercicio a mostrar se compone de nombre e imagen
    textColor: Color,
    onExerciseClick: (ExerciseCategory) -> Unit,
    isPortrait: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(if (isPortrait) 8.dp else 4.dp)
            .clickable { onExerciseClick(exercise) }
    ) {
        Image( // Imagen del ejercicio
            painter = painterResource(id = exercise.imageResource),
            contentDescription = stringResource(R.string.imagen_de, exercise.name),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(if (isPortrait) 70.dp else 60.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(if (isPortrait) 6.dp else 4.dp))
        Text( // Nombre del ejercicio
            exercise.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = if (isPortrait) 14.sp else 12.sp
            ),
            color = textColor
        )
    }
}

// Otras categorías de la pantalla de inicio (Entrenamiento, Academia, Progreso, Tienda)
@Composable
fun OtherCategories(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    isPortrait: Boolean
) {
    Column( // Muestra las categorías en dos filas con dos elementos cada una
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryItem( // Cada categoría se muestra como un Card con imagen y nombre
                categories[0],
                { onCategoryClick(categories[0]) },
                Modifier.weight(1f),
                isPortrait
            )
            CategoryItem(
                categories[1],
                { onCategoryClick(categories[1]) },
                Modifier.weight(1f),
                isPortrait
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryItem(
                categories[2],
                { onCategoryClick(categories[2]) },
                Modifier.weight(1f),
                isPortrait
            )
            CategoryItem(
                categories[3],
                { onCategoryClick(categories[3]) },
                Modifier.weight(1f),
                isPortrait
            )
        }
    }
}

// Cada categoría se muestra como un Card con imagen y nombre
@Composable
fun CategoryItem(
    category: Category,
    onCategoryClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPortrait: Boolean
) {
    Card(
        modifier = modifier
            .let { // Ajusta la relación de aspecto de la imagen
                if (isPortrait) {
                    it.aspectRatio(1f) // Cuadrado en portrait
                } else {
                    it.aspectRatio(2f)  // Más ancho que alto en landscape
                }
            }
            .padding(4.dp)
            .clickable { onCategoryClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Box(modifier = Modifier.fillMaxSize()) { // Contenedor de la imagen y el nombre
            Image(
                painter = painterResource(id = category.imageResource),
                contentDescription = stringResource(R.string.imagen_de, category.name),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box( // Fondo oscuro con transparencia para mejorar la legibilidad del texto
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.1f))
            )

            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}

// Función para mostrar el blog
@Composable
fun Blog(
    blogPost: BlogPost,
    cardColor: Color,
    onBlogClick: () -> Unit,
    isPortrait: Boolean
) {
    Card( // Muestra el blog como un Card con imagen y título
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isPortrait) 100.dp else 80.dp)
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clickable { onBlogClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = blogPost.imageResource),
                contentDescription = stringResource(R.string.imagen_de_blog),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Row( // Título del blog centrado en la imagen de fondo con un icono
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Newspaper,
                    contentDescription = stringResource(R.string.icono_de_blog),
                    tint = Color.White,
                    modifier = Modifier
                        .padding(horizontal = if (isPortrait) 8.dp else 4.dp)
                        .size(if (isPortrait) 30.dp else 24.dp)
                )
                Text(
                    text = blogPost.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = if (isPortrait) 24.sp else 20.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(if (isPortrait) 8.dp else 4.dp)
                )
            }
        }
    }
}

// Saludo personalizado en la barra superior
@Composable
fun WelcomeMessage(userName: String, textColor: Color, isPortrait: Boolean) {
    Text(
        stringResource(R.string.hola_user, userName),
        style = MaterialTheme.typography.headlineSmall.copy(
            fontSize = if (isPortrait) 24.sp else 20.sp
        ),
        color = textColor,
        modifier = Modifier.padding(
            horizontal = 8.dp,
            vertical = 4.dp
        )
    )
}

// Barra de navegación inferior común a todas las pantallas
@Composable
fun CommonBottomBar(
    navController: NavHostController,
    isDarkTheme: Boolean,
    isPortrait: Boolean
) {
    val backgroundColor = if (isDarkTheme) DarkBlueDark else DarkBlueLight
    val contentColor = Color.White

    // Elementos de la barra de navegación
    data class NavItem(val icon: ImageVector, val label: String, val route: String)

    val items = listOf( // Lista de elementos de la barra de navegación
        NavItem(Icons.Rounded.Timer, stringResource(R.string.temporizador), Screen.Timer.route),
        NavItem(Icons.Rounded.Home, stringResource(R.string.inicio), Screen.Home.route),
        NavItem(
            Icons.Rounded.DateRange,
            stringResource(R.string.calendario),
            Screen.Calendar.route
        )
    )

    // Barra de navegación con los elementos de la lista
    NavigationBar(
        containerColor = backgroundColor,
        modifier = Modifier.height(if (isPortrait) 80.dp else 64.dp)
    ) {
        items.forEach { item -> // Cada elemento es un NavigationBarItem
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(if (isPortrait) 24.dp else 20.dp)
                    )
                },
                label = {
                    Text(
                        item.label, fontSize = if (isPortrait) 12.sp else 10.sp
                    )
                },
                // Navega a la ruta del elemento si está seleccionado
                selected = navController.currentDestination?.route == item.route,
                // Navega a la ruta del elemento al hacer clic
                // También cierra el cajón de navegación si está abierto para evitar superposiciones
                onClick = {
                    when (item.route) {
                        Screen.Home.route -> {
                            if (navController.currentDestination?.route != Screen.Home.route) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                        else -> {
                            if (navController.currentDestination?.route != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(Screen.Home.route)
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentOrange,
                    unselectedIconColor = contentColor.copy(alpha = 0.7f),
                    selectedTextColor = AccentOrange,
                    unselectedTextColor = contentColor.copy(alpha = 0.7f),
                    indicatorColor = contentColor.copy(alpha = 0.1f) // Color de fondo del elemento seleccionado
                )
            )
        }
    }
}

// Función para mostrar el menú lateral
@Composable
fun DrawerContent(
    userName: String,
    backgroundColor: Color,
    drawerState: DrawerState,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSocialClick: (String) -> Unit,
    onShowContactDialog: () -> Unit,
    onLogoutClick: () -> Unit,
    isPortrait: Boolean
) {
    // CoroutineScope para manejar las acciones de cierre del cajón de navegación
    val scope = rememberCoroutineScope()

    fun handleClick(action: () -> Unit) {
        scope.launch {
            drawerState.close() // Cierra el cajón de navegación antes de ejecutar la acción
            action()
        }
    }

    // Contenido del cajón de navegación
    ModalDrawerSheet(
        modifier = Modifier.width(if (isPortrait) 300.dp else 280.dp),
        drawerContainerColor = backgroundColor
    ) {
        LazyColumn { // Columna con elementos del cajón de navegación (Header, Secciones, Cerrar Sesión) con scroll
            item { // Header con foto de perfil y nombre
                DrawerHeader(userName, isPortrait)
            }

            item { // Sección Usuario
                DrawerSection(title = stringResource(R.string.usuario), isPortrait = isPortrait) {
                    DrawerItem( // Elemento de la sección Usuario
                        icon = Icons.Rounded.Person,
                        label = stringResource(R.string.mi_perfil),
                        onClick = {
                            handleClick(onProfileClick)

                        },
                        isPortrait = isPortrait
                    )
                    DrawerItem(
                        icon = Icons.Rounded.Settings,
                        label = stringResource(R.string.ajustes),
                        onClick = {
                            handleClick(onSettingsClick)
                        },
                        isPortrait = isPortrait
                    )
                }
            }
            item { // Sección Social
                DrawerSection(title = stringResource(R.string.social), isPortrait = isPortrait) {
                    DrawerItem(
                        icon = R.drawable.instagram_icon,
                        label = stringResource(R.string.instagram),
                        onClick = {
                            handleClick { onSocialClick("instagram") }
                        },
                        isPortrait = isPortrait
                    )
                    DrawerItem(
                        icon = R.drawable.youtube_icon,
                        label = stringResource(R.string.youtube),
                        onClick = {
                            handleClick { onSocialClick("youtube") }
                        },
                        isPortrait = isPortrait
                    )
                    DrawerItem(
                        icon = R.drawable.spotify_icon,
                        label = stringResource(R.string.spotify),
                        onClick = {
                            handleClick { onSocialClick("spotify") }
                        },
                        isPortrait = isPortrait
                    )
                }
            }

            item {  // Sección ayuda
                DrawerSection(title = "Ayuda", isPortrait = isPortrait) {
                    DrawerItem(
                        icon = Icons.AutoMirrored.Rounded.Help,
                        label = stringResource(R.string.ayuda),
                        onClick = {
                            onShowContactDialog()
                        },
                        isPortrait = isPortrait
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider(Modifier.fillMaxWidth())
                // Botón de Cerrar Sesión
                DrawerItem(
                    icon = Icons.AutoMirrored.Rounded.Logout,
                    label = stringResource(R.string.cerrar_sesi_n),
                    onClick = {
                        handleClick(onLogoutClick)
                    },
                    isPortrait = isPortrait
                )
            }
        }
    }
}

// Función para el encabezado del menú lateral
// Muestra la foto de perfil y el nombre del usuario
@Composable
private fun DrawerHeader(userName: String, isPortrait: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isPortrait) 200.dp else 160.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .padding(if (isPortrait) 16.dp else 12.dp)
                .align(Alignment.BottomStart)
        ) {
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = stringResource(R.string.foto_de_perfil),
                modifier = Modifier
                    .size(if (isPortrait) 60.dp else 50.dp)
                    .clip(CircleShape) // Foto de perfil redonda
            )
            Spacer(modifier = Modifier.height(if (isPortrait) 16.dp else 12.dp))
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = if (isPortrait) 20.sp else 18.sp
                ),
                color = Color.White
            )
        }
    }
}

// Función para mostrar las secciones del menú lateral
@Composable
private fun DrawerSection(
    title: String,
    isPortrait: Boolean,
    content: @Composable () -> Unit
) {
    Column {
        Text( // Título de la sección
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(
                start = if (isPortrait) 28.dp else 24.dp,
                top = if (isPortrait) 16.dp else 12.dp,
                bottom = if (isPortrait) 8.dp else 6.dp
            )
        )
        content() // Contenido de la sección
    }
}

// Función para mostrar los elementos del menú lateral (icono y etiqueta)
@Composable
private fun DrawerItem(
    icon: Any,
    label: String,
    onClick: () -> Unit,
    isPortrait: Boolean
) {
    NavigationDrawerItem(
        icon = {
            when (icon) { // Icono puede ser un ImageVector o un recurso de imagenes
                is ImageVector -> Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(if (isPortrait) 24.dp else 20.dp)
                )

                is Int -> Icon(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    modifier = Modifier.size(if (isPortrait) 24.dp else 20.dp)
                )
            }
        },
        label = { // Etiqueta del elemento
            Text(
                label, fontSize = if (isPortrait) 14.sp else 12.sp
            )
        },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = if (isPortrait) 12.dp else 8.dp)
    )
}