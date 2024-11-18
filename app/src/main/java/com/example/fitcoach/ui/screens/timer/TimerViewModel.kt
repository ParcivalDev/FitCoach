package com.example.fitcoach.ui.screens.timer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Maneja la lógica del temporizador
class TimerViewModel : ViewModel() {
    // Guarda la tarea actual del temporizador
    private var timerJob: Job? = null

    // Estado actual del temporizador. Privada para evitar cambios externos
    private val _timerState = MutableStateFlow(TimerState())

    // Estado del temporizador accesible desde otras clases
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    // Define el estado inicial del temporizador
    data class TimerState(
        val hours: Int = 0,
        val minutes: Int = 2,
        val seconds: Int = 0,
        val isActive: Boolean = false,
        val totalSeconds: Int = 120
    )

    // Se inicializa el servicio de notificaciones
    fun initialize(context: Context) {
        NotificationService.createNotificationChannel(context)
    }

    // Actualiza el tiempo del temporizador
    fun updateTime(hours: Int, minutes: Int, seconds: Int) {
        // Si el temporizador no está activo, se actualiza el tiempo
        if (!_timerState.value.isActive) {
            val totalSeconds = (hours * 3600) + (minutes * 60) + seconds
            _timerState.value = TimerState(hours, minutes, seconds, false, totalSeconds)
        }
    }

    // Inicia el temporizador
    private fun startTimer(context: Context) {
        // Si el temporizador tiene tiempo restante y no está activo
        if (_timerState.value.totalSeconds > 0 && !_timerState.value.isActive) {
            // Se activa el temporizador
            _timerState.value = _timerState.value.copy(isActive = true)

            // Se inicia la tarea del temporizador
            timerJob = viewModelScope.launch {
                var remaining = _timerState.value.totalSeconds
                while (remaining > 0 && _timerState.value.isActive) {
                    delay(1000)
                    remaining--

                    // Se actualiza el tiempo restante
                    val hours = remaining / 3600
                    val minutes = (remaining % 3600) / 60
                    val seconds = remaining % 60

                    // Se actualiza el estado del temporizador con el nuevo tiempo
                    _timerState.value = _timerState.value.copy(
                        hours = hours,
                        minutes = minutes,
                        seconds = seconds,
                        totalSeconds = remaining
                    )
                }
                // Si el tiempo restante es 0, se muestra la notificación y se reinicia el temporizador
                if (remaining == 0) {
                    NotificationService.timerNotification(context)
                    resetTimer()
                }
            }
        }
    }

    // Pausa el temporizador
    private fun pauseTimer() {
        // Marca como inactivo el temporizador y cancela la cuenta atrás
        _timerState.value = _timerState.value.copy(isActive = false)
        timerJob?.cancel()
    }

    // Reinicia el temporizador
    fun resetTimer() {
        pauseTimer()
        _timerState.value = TimerState()
    }

    // Inicia o pausa el temporizador
    fun toggleTimer(context: Context) {
        if (_timerState.value.isActive) pauseTimer() else startTimer(context)
    }

    // Cancela la tarea del temporizador al cerrar la aplicación
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}