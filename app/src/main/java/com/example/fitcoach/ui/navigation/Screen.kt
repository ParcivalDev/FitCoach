package com.example.fitcoach.ui.navigation



// Clase que define las rutas de navegación de la aplicación
sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Timer : Screen("timer")
    object Calendar : Screen("calendar")
    object Blog : Screen("blog")

    object ExerciseLibrary : Screen("exercises?muscleGroup={muscleGroup}") {
        fun createRoute(muscleGroup: String? = null) =
            "exercises" + (muscleGroup?.let { "?muscleGroup=$it" } ?: "")
    }

    object Academy : Screen("academy?moduleId={moduleId}") {
        fun createRoute(moduleId: String? = null) =
            "academy" + (moduleId?.let { "?moduleId=$it" } ?: "")
    }
}