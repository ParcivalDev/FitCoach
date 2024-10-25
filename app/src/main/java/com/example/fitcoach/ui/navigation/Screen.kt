package com.example.fitcoach.ui.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Timer : Screen("timer")

}