package com.example.fitcoach.ui.screens.timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private var timerJob: Job? = null

    var hours by mutableStateOf(0)
        private set

    var minutes by mutableStateOf(2)
        private set

    var seconds by mutableStateOf(0)
        private set

    var isActive by mutableStateOf(false)
        private set

    private fun updateTime(block: () -> Unit) {
        if (!isActive) {
            block()
        }
    }

    fun updateHours(newHours: Int) = updateTime { hours = newHours }
    fun updateMinutes(newMinutes: Int) = updateTime { minutes = newMinutes }
    fun updateSeconds(newSeconds: Int) = updateTime { seconds = newSeconds }

    fun startTimer() {
        val totalSeconds = (hours * 3600) + (minutes * 60) + seconds
        if (totalSeconds > 0) {
            isActive = true
            timerJob = viewModelScope.launch {
                var remaining = totalSeconds
                while (remaining > 0 && isActive) {
                    delay(1000)
                    remaining--
                    hours = remaining / 3600
                    minutes = (remaining % 3600) / 60
                    seconds = remaining % 60
                }
                if (remaining == 0) resetTimer()
            }
        }
    }

    fun pauseTimer() {
        isActive = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        hours = 0
        minutes = 2
        seconds = 0
    }

    fun toggleTimer() {
        if (isActive) pauseTimer() else startTimer()
    }
}
