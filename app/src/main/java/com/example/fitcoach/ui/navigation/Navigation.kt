package com.example.fitcoach.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitcoach.ui.screens.home.HomeScreen
import com.example.fitcoach.ui.screens.timer.TimerScreen
import com.example.fitcoach.ui.screens.login.LoginScreen
import com.example.fitcoach.ui.screens.login.LoginViewModel
import com.example.fitcoach.ui.screens.splash.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            val loginViewModel = LoginViewModel()
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        } // Esto es para que no se pueda volver a la pantalla de login
                    }
                },
                vm = loginViewModel
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Timer.route) {
            TimerScreen(
                navController = navController
            )
        }
    }
}
