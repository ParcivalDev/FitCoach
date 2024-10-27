package com.example.fitcoach.ui.screens.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel : ViewModel() {
    // Estados básicos
    var currentMonth by mutableStateOf(YearMonth.now())
        private set

    var selectedDate by mutableStateOf<LocalDate?>(null)
        private set

    var workoutNotes by mutableStateOf<Map<LocalDate, WorkoutNote>>(emptyMap())
        private set

    var showDialog by mutableStateOf(false)
        private set

    // Funciones para actualizar estados
    @RequiresApi(Build.VERSION_CODES.O)
    fun onMonthChange(isNext: Boolean) {
        currentMonth = if (isNext) {
            currentMonth.plusMonths(1)
        } else {
            currentMonth.minusMonths(1)
        }
    }

    fun onDateSelect(date: LocalDate) {
        selectedDate = date
    }

    fun onShowDialog() {
        showDialog = true
    }

    fun onHideDialog() {
        showDialog = false
    }

    fun onSaveNote(date: LocalDate, note: String, rating: WorkoutRating) {
        workoutNotes = workoutNotes.toMutableMap().apply {
            put(date, WorkoutNote(note, rating))
        }
        showDialog = false
    }
}
