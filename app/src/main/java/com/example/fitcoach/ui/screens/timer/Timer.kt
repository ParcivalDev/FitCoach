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
import com.example.fitcoach.ui.theme.BackgroundDark
import com.example.fitcoach.ui.theme.BackgroundLight


// Función principal que muestra la pantalla del Temporizador
@SuppressLint("SourceLockedOrientationActivity") // Evita advertencias de bloqueo de orientación
@Composable
fun TimerScreen(
    navController: NavHostController, // Controlador para la navegación
    timerViewModel: TimerViewModel // viewModel para la lógica del temporizador
) {
    // Estado del temporizador actual
    val timerState by timerViewModel.timerState.collectAsState()

    // Contexto actual para manejar la orientación y solicitar permisos
    val context = LocalContext.current

    // Inicializa el servicio de notificaciones
    LaunchedEffect(Unit) {
        timerViewModel.initialize(context)
    }

    // Bloquea la orientación
    DisposableEffect(Unit) {
        val activity = context as Activity
        // Guarda la orientación original de la pantalla
        val originalOrientation = activity.requestedOrientation
        // Fuerza la orientación a vertical
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Restaura la orientación original cuando sales de la pantalla para evitar problemas con otras pantallas
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }


    // Solicitud de permiso para notificaciones en Android 13 y superiores
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PermissionRequestEffect(
            permission = Manifest.permission.POST_NOTIFICATIONS
        ) { granted ->
            if (!granted) {
                // Muestra un mensaje informativo si el permiso no está concedido
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
        val backgroundColor = if (isSystemInDarkTheme()) BackgroundDark else BackgroundLight

        // Columna con el contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues), // Padding para evitar que el contenido se superponga con la barra de navegación
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Componentes del temporizador
            Temporizador(
                isSystemInDarkTheme(),
                hours = timerState.hours,
                minutes = timerState.minutes,
                seconds = timerState.seconds,
                isActive = timerState.isActive,
                onHoursChange = { newHours ->
                    timerViewModel.updateTime(
                        newHours,
                        timerState.minutes,
                        timerState.seconds
                    )
                },
                onMinutesChange = { newMinutes ->
                    timerViewModel.updateTime(
                        timerState.hours,
                        newMinutes,
                        timerState.seconds
                    )
                },
                onSecondsChange = { newSeconds ->
                    timerViewModel.updateTime(
                        timerState.hours,
                        timerState.minutes,
                        newSeconds
                    )
                }
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
                            timerViewModel.updateTime(hours = 0, minutes = 1, seconds = 30)
                            timerViewModel.toggleTimer(context)
                        },
                        isSystemInDarkTheme()
                    )
                    BotonSeleccionTiempo(
                        texto = stringResource(R.string.tres_min),
                        onClick = {
                            timerViewModel.updateTime(hours = 0, minutes = 3, seconds = 0)
                            timerViewModel.toggleTimer(context)
                        },
                        isSystemInDarkTheme()
                    )
                }

            }
            Spacer(modifier = Modifier.weight(1f))

            // Controles del temporizador (Iniciar/Pausar, Reiniciar)
            TimerControls(
                isActive = timerState.isActive,
                onToggle = { timerViewModel.toggleTimer(context) },
                onReset = { timerViewModel.resetTimer() }
            )
            Spacer(modifier = Modifier.weight(0.2f))

        }
    }
}

// Función para solicitar permisos en Android 13 y superiores
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