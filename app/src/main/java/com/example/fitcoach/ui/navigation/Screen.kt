package com.example.fitcoach.ui.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

}