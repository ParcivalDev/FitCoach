package com.example.fitcoach.ui.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private var timerJob: Job? = null

    private val _hours = MutableStateFlow(0)
    val hours = _hours.asStateFlow()

    private val _minutes = MutableStateFlow(2)
    val minutes = _minutes.asStateFlow()

    private val _seconds = MutableStateFlow(0)
    val seconds = _seconds.asStateFlow()

    private val _isActive = MutableStateFlow(false)
    val isActive = _isActive.asStateFlow()

    private fun updateTime(block: () -> Unit) {
        if (!_isActive.value) {
            block()
        }
    }

    fun updateHours(newHours: Int) = updateTime { _hours.value = newHours }
    fun updateMinutes(newMinutes: Int) = updateTime { _minutes.value = newMinutes }
    fun updateSeconds(newSeconds: Int) = updateTime { _seconds.value = newSeconds }

    fun startTimer() {
        val totalSeconds = (_hours.value * 3600) + (_minutes.value * 60) + _seconds.value
        if (totalSeconds > 0) {
            _isActive.value = true
            timerJob = viewModelScope.launch {
                var remaining = totalSeconds
                while (remaining > 0 && _isActive.value) {
                    delay(1000)
                    remaining--
                    _hours.value = remaining / 3600
                    _minutes.value = (remaining % 3600) / 60
                    _seconds.value = remaining % 60
                }
                if (remaining == 0) resetTimer()
            }
        }
    }

    fun pauseTimer() {
        _isActive.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _hours.value = 0
        _minutes.value = 2
        _seconds.value = 0
    }

    fun toggleTimer() {
        if (_isActive.value) pauseTimer() else startTimer()
    }
}