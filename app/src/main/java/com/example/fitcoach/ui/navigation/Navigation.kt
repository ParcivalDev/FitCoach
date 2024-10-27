package com.example.fitcoach.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.fitcoach.ui.screens.home.HomeScreen
import com.example.fitcoach.ui.screens.home.HomeViewModel
import com.example.fitcoach.ui.screens.timer.TimerScreen
import com.example.fitcoach.ui.screens.login.LoginScreen
import com.example.fitcoach.ui.screens.login.LoginViewModel
import com.example.fitcoach.ui.screens.splash.SplashScreen
import com.example.fitcoach.ui.screens.timer.TimerViewModel

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
            val loginViewModel: LoginViewModel = viewModel()
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
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(navController = navController, vm = homeViewModel)
        }
        composable(Screen.Timer.route) {
            val timerViewModel: TimerViewModel = viewModel()
            TimerScreen(
                navController = navController,
                vm = timerViewModel
            )
        }
    }
}
