package com.example.fitcoach.ui.navigation

// Clase que define las rutas de navegación de la aplicación
sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Timer : Screen("timer")
    object Calendar : Screen("calendar")
    object MuscleLibrary : Screen("muscleLibrary")
}