package com.example.fitcoach.ui.screens.exercises

// Data class que representa un ejercicio
data class Exercise(
    val name: String = "", // Nombre del ejercicio
    val muscleGroup: String = "", // Grupo muscular al que pertenece el ejercicio
    val vimeoId: String = "", // ID del video en Vimeo
    val vimeoHash: String = "" // Hash para obtener la imagen del video
)