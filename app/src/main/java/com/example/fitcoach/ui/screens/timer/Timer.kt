package com.example.fitcoach.ui.screens.timer


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitcoach.R
import com.example.fitcoach.ui.screens.home.components.CommonBottomBar
import com.example.fitcoach.ui.theme.DarkBlueDark


// Función principal que muestra la pantalla del Temporizador
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun TimerScreen(
    navController: NavHostController, // Controlador para la navegación
    timerViewModel: TimerViewModel // viewModel para la lógica del temporizador
) {

    val timerState by timerViewModel.timerState.collectAsState()

    // Contexto actual para manejar la orientación y solicitar permisos
    val context = LocalContext.current

    // Bloquea la orientación
    DisposableEffect(Unit) {
        val activity = context as Activity
        val originalOrientation = activity.requestedOrientation
        // Fuerza la orientación a vertical
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Restaura la orientación original cuando sales de la pantalla para evitar problemas con otras pantallas
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }


    // Solicitud de permiso para notificaciones
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PermissionRequestEffect(
            permission = Manifest.permission.POST_NOTIFICATIONS
        ) { granted ->
            if (!granted) {
                // Solo mostrar el Toast informativo
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_not_timer),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    Scaffold(
        // Barra de navegación inferior
        bottomBar = { CommonBottomBar(navController, isSystemInDarkTheme()) }
    ) { paddingValues ->
        // Columna con el contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlueDark)
                .padding(paddingValues), // Padding para evitar que el contenido se superponga con la barra de navegación
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Componentes del temporizador
            Temporizador(
                hours = timerState.hours,
                minutes = timerState.minutes,
                seconds = timerState.seconds,
                isActive = timerState.isActive,
                onHoursChange = { newHours -> timerViewModel.updateTime(newHours, timerState.minutes, timerState.seconds) },
                onMinutesChange = { newMinutes -> timerViewModel.updateTime(timerState.hours, newMinutes, timerState.seconds) },
                onSecondsChange = { newSeconds -> timerViewModel.updateTime(timerState.hours, timerState.minutes, newSeconds) }
            )
            Spacer(modifier = Modifier.weight(0.2f))

            // Botones de acceso rápido solo visibles cuando el timer no está activo
            if (!timerState.isActive) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Botón para seleccionar 1 minuto y 30 segundos
                    BotonSeleccionTiempo(
                        texto = stringResource(R.string.min_y_medio),
                        onClick = {
                            timerViewModel.updateTime(minutes = 1, seconds = 30)
                            timerViewModel.toggleTimer()
                        }
                    )
                    BotonSeleccionTiempo(
                        texto = stringResource(R.string.dos_min),
                        onClick = {
                            timerViewModel.updateTime(minutes = 2, seconds = 0)
                            timerViewModel.toggleTimer()
                        }
                    )
                    BotonSeleccionTiempo(
                        texto = stringResource(R.string.tres_min),
                        onClick = {
                            timerViewModel.updateTime(minutes = 3, seconds = 0)
                            timerViewModel.toggleTimer()
                        }
                    )
                }

            }
            Spacer(modifier = Modifier.weight(1f))

            // Controles del temporizador (Iniciar/Pausar, Reiniciar)
            TimerControls(
                isActive = timerState.isActive,
                onToggle = { timerViewModel.toggleTimer() },
                onReset = { timerViewModel.resetTimer() }
            )
            Spacer(modifier = Modifier.weight(0.2f))

        }
    }
}

@Composable
fun PermissionRequestEffect(permission: String, onResult: (Boolean) -> Unit) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        onResult(it)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permission)
    }
}