package com.example.fitcoach.ui.screens.calendar


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel : ViewModel() {
    // Estado para el mes actual
    var currentMonth by mutableStateOf(YearMonth.now())
        private set // Solo se puede modificar desde el ViewModel

    // Estado para la fecha seleccionada
    var selectedDate by mutableStateOf<LocalDate?>(null)
        private set

    // Estado para las notas de entrenamiento
    var workoutNotes by mutableStateOf<Map<LocalDate, WorkoutNote>>(emptyMap())
        private set

    // Estado para mostrar/ocultar el diálogo
    var showDialog by mutableStateOf(false)
        private set

    // Estado para mostrar/ocultar el indicador de carga
    var isLoading by mutableStateOf(false)
        private set

    // Estado para mostrar mensajes de error
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Inicializar el día seleccionado cuando se crea el ViewModel
    init {
        selectedDate = LocalDate.now()
        loadNotes() // Cargar notas al iniciar
    }

    // Función para cargar las notas de entrenamiento
    private fun loadNotes() {
        CalendarRepository.loadNotes { result -> // Cargar notas desde Firestore
            when (result) { // Actualizar estados según el resultado
                // Si es un resultado exitoso, guarda las notas y actualiza estados
                is CalendarResult.Success -> {
                    workoutNotes = result.data.mapKeys { (key, _) -> // mapkeys para convertir la clave a LocalDate
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

    // Funciones para cambiar el mes actual
    fun onMonthChange(isNext: Boolean) {
        currentMonth = if (isNext) {
            currentMonth.plusMonths(1)
        } else {
            currentMonth.minusMonths(1)
        }
        selectedDate = null // Limpiar la fecha seleccionada
    }

    // Función para seleccionar una fecha
    fun onDateSelect(date: LocalDate) {
        // Actualizar la fecha seleccionada recibida como argumento
        selectedDate = date
    }

    fun onShowDialog() {
        showDialog = true
    }

    fun onHideDialog() {
        showDialog = false
    }

    fun onSaveNote(date: LocalDate, note: String, rating: WorkoutRating) {
        val workoutNote = WorkoutNote(note, rating) // Crear objeto WorkoutNote
        isLoading = true

        // Guardar nota en Firestore pasando la fecha a String y el objeto WorkoutNote
        CalendarRepository.saveNote(date.toString(), workoutNote) { result ->
            when (result) {
                is CalendarResult.Success -> {
                    // Actualizar el mapa de notas con la nueva nota
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

    // Función para eliminar una nota
    fun onDeleteNote(date: LocalDate) {
        isLoading = true

        CalendarRepository.deleteNote(date.toString()) { result ->
            when (result) {
                is CalendarResult.Success -> {
                    // Actualizar el mapa de notas eliminando la nota
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
