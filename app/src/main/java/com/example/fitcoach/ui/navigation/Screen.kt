package com.example.fitcoach.ui.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object Login : Screen("login")
    object Home : Screen("home")
    object Timer : Screen("timer_screen")

}