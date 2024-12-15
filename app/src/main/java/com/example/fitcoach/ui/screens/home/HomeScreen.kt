package com.example.fitcoach.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fitcoach.ui.navigation.Screen
import com.example.fitcoach.ui.screens.chat.ChatDialog
import com.example.fitcoach.ui.screens.home.components.Blog
import com.example.fitcoach.ui.screens.home.components.CommonBottomBar
import com.example.fitcoach.ui.screens.home.components.DrawerContent
import com.example.fitcoach.ui.screens.home.components.ExerciseLibrary
import com.example.fitcoach.ui.screens.home.components.OtherCategories
import com.example.fitcoach.ui.screens.home.components.TopAppBarHome
import com.example.fitcoach.ui.screens.home.components.WelcomeMessage
import com.example.fitcoach.ui.theme.AccentOrange
import com.example.fitcoach.ui.theme.BackgroundDark
import com.example.fitcoach.ui.theme.BackgroundLight
import com.example.fitcoach.ui.theme.CardDark
import com.example.fitcoach.ui.theme.CardLight
import com.example.fitcoach.utils.ContactDialog
import com.example.fitcoach.utils.DialogUtils
import com.example.fitcoach.utils.UnderDevelopmentDialog
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}

// Pantalla principal de la aplicación
@Composable
fun HomeScreen(navController: NavHostController, vm: HomeViewModel = viewModel()) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight
    val textColor = if (isDarkTheme) Color.White else Color.Black

    // Estado del cajón de navegación por defecto cerrado
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Obtener la configuración actual del dispositivo
    val configuration = LocalConfiguration.current
    // Determinar si el dispositivo está en modo vertical
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Inicializar SharedPreferences al lanzar la pantalla para obtener el nombre de usuario¿?¿?¿?¿?
    LaunchedEffect(Unit) {
        vm.initSharedPreferences(context)
    }

    // Muestra el diálogo de contacto si es necesario
    if (vm.showContactDialog) {
        ContactDialog(onDismiss = { vm.onDismissContactDialog() },
            onWhatsAppClick = { DialogUtils.handleWhatsApp(context) },
            onEmailClick = { DialogUtils.handleEmail(context) })
    }

    // Muestra el diálogo de función en desarrollo si es necesario
    if (vm.showUnderDevelopmentDialog) {
        UnderDevelopmentDialog(onDismiss = vm::dismissDevelopmentDialog)
    }

    if (vm.showChat) {
        ChatDialog(
            onDismiss = { vm.dismissChatDialog() }
        )
    }


    // Pantalla principal
    // Muestra el cajón de navegación y el contenido principal
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent( // Contenido del cajón de navegación
                userName = vm.userName,
                backgroundColor = backgroundColor,
                drawerState = drawerState,
                onProfileClick = { vm.showDevelopmentDialog() },
                onSettingsClick = { vm.showDevelopmentDialog() },
                onSocialClick = { network -> vm.onSocialClick(network, context) },
                onShowContactDialog = { vm.onShowContactDialog() },
                onLogoutClick = { // Cerrar sesión
                    vm.onLogout {
                        navController.navigate(Screen.Login.route) {
                            // Limpiar la pila de navegación al cerrar sesión
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                },
                isPortrait = isPortrait
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBarHome(
                    isDarkTheme = isDarkTheme,
                    onProfileClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onNotificationClick = { vm.showDevelopmentDialog() },
                    isPortrait = isPortrait
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { vm.showChatDialog() },
                    containerColor = AccentOrange,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Chat,
                        contentDescription = "Abrir chat"
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = { CommonBottomBar(navController, isDarkTheme, isPortrait) },
            containerColor = backgroundColor
        ) { paddingValues ->
            LazyColumn(
                // Contenido principal con scroll
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
            ) {
                item {
                    WelcomeMessage(
                        userName = vm.userName,
                        textColor = textColor,
                        isPortrait = isPortrait
                    )
                }
                // Mostrar la biblioteca de ejercicios
                item {
                    ExerciseLibrary(
                        exercises = vm.exercises,
                        textColor = textColor,
                        /*onExerciseClick = { exercise ->
                            navController.navigate("exercises?muscleGroup=${exercise.name}")
                        },*/
                        // En HomeScreen.kt, dentro de ExerciseLibrary
                        onExerciseClick = { exercise ->
                            navController.navigate(Screen.ExerciseLibrary.createRoute(exercise.name))
                        },
                        onSeeAllClick = { navController.navigate("exercises") },
                        isPortrait = isPortrait
                    )
                }
                // Mostrar las demás funciones
                item {
                    OtherCategories(
                        categories = vm.categories,
                        /*onCategoryClick = { category ->
                            vm.onCategoryClick(category, navController::navigate)
                        },*/
                        onCategoryClick = { category ->
                            if (category.name == "Academia") {
                                navController.navigate(Screen.Academy.route)
                            } else if (category.name == "Tienda") {
                                navController.navigate(Screen.Store.route)
                            } else {
                                vm.showDevelopmentDialog()
                            }
                        },
                        isPortrait = isPortrait
                    )
                }
                // Mostrar las últimas noticias
                item {
                    Blog(
                        blogPost = vm.blogPost,
                        cardColor = cardColor,
                        onBlogClick = { navController.navigate(Screen.Blog.route) },
                        isPortrait = isPortrait
                    )
                }
            }
        }
    }
}
