package com.example.fitcoach.ui.screens.calendar


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.YearMonth

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


    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Inicializar el día seleccionado cuando se crea el ViewModel
    init {
        selectedDate = LocalDate.now()
        loadNotes()
    }

    private fun loadNotes() {
        CalendarRepository.loadNotes { result ->
            when (result) {
                is CalendarResult.Success -> {
                    workoutNotes = result.data.mapKeys { (key, _) ->
                        LocalDate.parse(key)
                    }
                    isLoading = false
                    errorMessage = null
                }
                is CalendarResult.Error -> {
                    isLoading = false
                    errorMessage = result.exception.localizedMessage
                }
                CalendarResult.Loading -> {
                    isLoading = true
                    errorMessage = null
                }
            }
        }
    }

    // Funciones para actualizar estados
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
        val workoutNote = WorkoutNote(note, rating)
        isLoading = true

        CalendarRepository.saveNote(date.toString(), workoutNote) { result ->
            when (result) {
                is CalendarResult.Success -> {
                    workoutNotes = workoutNotes.toMutableMap().apply {
                        put(date, workoutNote)
                    }
                    showDialog = false
                    isLoading = false
                    errorMessage = null
                }
                is CalendarResult.Error -> {
                    isLoading = false
                    errorMessage = result.exception.localizedMessage
                }
                CalendarResult.Loading -> {
                    isLoading = true
                    errorMessage = null
                }
            }
        }
    }

    fun onDeleteNote(date: LocalDate) {
        isLoading = true

        CalendarRepository.deleteNote(date.toString()) { result ->
            when (result) {
                is CalendarResult.Success -> {
                    workoutNotes = workoutNotes.toMutableMap().apply {
                        remove(date)
                    }
                    isLoading = false
                    errorMessage = null
                }
                is CalendarResult.Error -> {
                    isLoading = false
                    errorMessage = result.exception.localizedMessage
                }
                CalendarResult.Loading -> {
                    isLoading = true
                    errorMessage = null
                }
            }
        }
    }

    // Función para limpiar errores
    fun clearError() {
        errorMessage = null
    }
}
