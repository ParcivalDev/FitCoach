package com.example.fitcoach.ui.screens.timer

import android.app.Application
import androidx.lifecycle.AndroidViewModel

// ViewModel para la pantalla del temporizador
// Hereda de AndroidViewModel para poder acceder al contexto de la aplicación
class TimerViewModel(application: Application) : AndroidViewModel(application) {
    init {
        // Inicializamos el TimerService con el contexto de la aplicación
        TimerService.initialize(application)
    }

    // Exponemos directamente el estado del service
    val timerState = TimerService.timerState

    // Funciones para interactuar con el temporizador
    fun updateTime(hours: Int = 0, minutes: Int = 2, seconds: Int = 0) =
        TimerService.updateTime(hours, minutes, seconds)

    // Iniciar el temporizador
    fun toggleTimer() = TimerService.toggleTimer()

    // Reiniciar el temporizador
    fun resetTimer() = TimerService.resetTimer()
}