package com.example.fitcoach.ui.screens.timer

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
object TimerService {
    // Inicializamos el scope para ejecutar el temporizador en segundo plano
    private val scope = CoroutineScope(Dispatchers.Default)

    // Guarda la tarea actual del temporizador
    private var timerJob: Job? = null
    private var context: Context? = null

    // Estado del temporizador privado y mutable
    private val _timerState = MutableStateFlow(TimerState())

    // Estado del temporizador público y de solo lectura
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    // Inicializa el servicio con el contexto de la aplicación
    fun initialize(context: Context) {
        this.context = context.applicationContext
        NotificationService.createNotificationChannel(context)
    }

    // Clase que representa el estado del temporizador
    data class TimerState(
        val hours: Int = 0,
        val minutes: Int = 2,
        val seconds: Int = 0,
        val isActive: Boolean = false,
        val totalSeconds: Int = 120
    )

    // Actualiza el tiempo del temporizador con los valores dados
    fun updateTime(hours: Int, minutes: Int, seconds: Int) {
        // Solo actualiza el tiempo si el temporizador no está activo
        if (!_timerState.value.isActive) {
            val totalSeconds = (hours * 3600) + (minutes * 60) + seconds
            _timerState.value = TimerState(hours, minutes, seconds, false, totalSeconds)
        }
    }

    // Inicia el temporizador
    private fun startTimer() {
        // Solo inicia el temporizador si el tiempo total es mayor a 0 y no está activo
        if (_timerState.value.totalSeconds > 0 && !_timerState.value.isActive) {
            _timerState.value = _timerState.value.copy(isActive = true)

            timerJob = scope.launch {
                var remaining = _timerState.value.totalSeconds
                // Bucle principal del temporizador
                while (remaining > 0 && _timerState.value.isActive) {
                    delay(1000)
                    remaining-- // Espera 1 segundo y resta 1 al tiempo restante

                    // Calcula las horas, minutos y segundos restantes
                    val hours = remaining / 3600
                    val minutes = (remaining % 3600) / 60
                    val seconds = remaining % 60

                    // Actualiza el estado del temporizador
                    _timerState.value = _timerState.value.copy(
                        hours = hours,
                        minutes = minutes,
                        seconds = seconds,
                        totalSeconds = remaining
                    )
                }
                // Si el tiempo restante es 0, muestra la notificación y reinicia el temporizador
                if (remaining == 0) {
                    context?.let { NotificationService.showTimerCompleteNotification(it) }
                    resetTimer()
                }
            }
        }
    }

    // Pausa el temporizador
    private fun pauseTimer() {
        // Con .copy() creamos una copia del estado actual y modificamos el valor isActive a false
        // Con copy solo se modifica el valor que se le pase, el resto se mantiene igual
        _timerState.value = _timerState.value.copy(isActive = false)
        timerJob?.cancel()
    }

    // Reinicia el temporizador
    fun resetTimer() {
        pauseTimer() // Pausa el temporizador
        _timerState.value = TimerState()
    }

    // Alterna entre iniciar y pausar el temporizador
    fun toggleTimer() {
        if (_timerState.value.isActive) pauseTimer() else startTimer()
    }
}