package com.example.fitcoach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fitcoach.ui.navigation.Navigation
import com.example.fitcoach.ui.theme.FitCoachTheme

// Clase principal que arranca la aplicación mostrando la primera ventana de la navegación (SplashScreen)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitCoachTheme {
                Navigation()
            }
        }
    }
}
