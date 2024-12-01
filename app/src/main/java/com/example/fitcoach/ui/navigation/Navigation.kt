package com.example.fitcoach.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.fitcoach.ui.screens.calendar.CalendarScreen
import com.example.fitcoach.ui.screens.calendar.CalendarViewModel
import com.example.fitcoach.ui.screens.exercises.ExerciseLibraryScreen
import com.example.fitcoach.ui.screens.exercises.ExerciseVideoPlayer
import com.example.fitcoach.ui.screens.home.HomeScreen
import com.example.fitcoach.ui.screens.home.HomeViewModel
import com.example.fitcoach.ui.screens.timer.TimerScreen
import com.example.fitcoach.ui.screens.login.LoginScreen
import com.example.fitcoach.ui.screens.login.LoginViewModel
import com.example.fitcoach.ui.screens.splash.SplashScreen
import com.example.fitcoach.ui.screens.timer.TimerViewModel
import kotlinx.coroutines.delay
// Función que define las rutas de navegación de la aplicación
@Composable
fun Navigation() {
    // Controlador de la navegación para manejar las transiciones entre pantallas
    val navController = rememberNavController()

    // Obtener el contexto
    val context = LocalContext.current
    // Intenta obtener la actividad actual
    val activity = context as? ComponentActivity
    // Verificar si la pantalla fue abierta desde una notificación
    val navigateToTimer = activity?.intent?.getBooleanExtra("navigateToTimer", false) ?: false

    // Si viene de la notificación, navegar al timer
    LaunchedEffect(navigateToTimer) {
        if (navigateToTimer) {
            delay(100) // Pequeña espera para asegurar que la navegación está lista
            navController.navigate(Screen.Timer.route) {
                popUpTo(Screen.SplashScreen.route) {
                    inclusive = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route // Primera pantalla en mostrarse
    ) {
        // Pantalla Splash
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                //Función que navega a la pantalla de login
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        // Elimina la pantalla anterior de la pila para evitar que pueda volver atrás
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                }
            )
        }
        // Pantalla Login
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },
                vm = loginViewModel
            )
        }
        // Pantalla Home
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(navController = navController, vm = homeViewModel)
        }
        // Pantalla Timer
        composable(Screen.Timer.route) {
            val timerViewModel: TimerViewModel = viewModel()
            TimerScreen(
                navController = navController,
                timerViewModel = timerViewModel
            )
        }
        // Pantalla Calendar
        composable(Screen.Calendar.route) {
            val calendarViewModel: CalendarViewModel = viewModel()
            CalendarScreen(
                navController = navController,
                vm = calendarViewModel,
            )
        }


        // POR REVISAR - Pantalla de la biblioteca de ejercicios
        composable("exercises?muscleGroup={muscleGroup}") { backStackEntry ->
            val muscleGroup = backStackEntry.arguments?.getString("muscleGroup")
            val homeViewModel: HomeViewModel = viewModel()
            ExerciseLibraryScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                initialMuscle = muscleGroup
            )
        }

        composable(
            route = "exercise_video/{vimeoId}/{name}"
        ) { backStackEntry ->
            val vimeoId = backStackEntry.arguments?.getString("vimeoId") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            ExerciseVideoPlayer(
                vimeoId = vimeoId,
                exerciseName = name,
                navController = navController
            )
        }

    }
}
