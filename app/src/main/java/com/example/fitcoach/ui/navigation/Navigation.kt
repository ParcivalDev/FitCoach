package com.example.fitcoach.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.fitcoach.ui.screens.calendar.CalendarScreen
import com.example.fitcoach.ui.screens.calendar.CalendarViewModel
import com.example.fitcoach.ui.screens.home.HomeScreen
import com.example.fitcoach.ui.screens.home.HomeViewModel
import com.example.fitcoach.ui.screens.timer.TimerScreen
import com.example.fitcoach.ui.screens.login.LoginScreen
import com.example.fitcoach.ui.screens.login.LoginViewModel
import com.example.fitcoach.ui.screens.splash.SplashScreen
import com.example.fitcoach.ui.screens.timer.TimerViewModel


@Composable
fun Navigation() {
    // Controlador de la navegación
    val navController = rememberNavController()


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
                vm = timerViewModel
            )
        }
        // Pantalla Calendar
        composable(Screen.Calendar.route) {
            val calendarViewModel: CalendarViewModel = viewModel()
            CalendarScreen(
                navController = navController,
                vm = calendarViewModel,
                //Función para volver a la pantalla anterior que sería Home
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
