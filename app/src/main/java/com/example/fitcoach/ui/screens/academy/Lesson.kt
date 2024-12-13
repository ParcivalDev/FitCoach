package com.example.fitcoach.ui.screens.academy


// Modelo de datos para representar una lección
data class Lesson(
    val name: String = "",
    val moduleId: String = "", // ID del módulo al que pertenece
    val vimeoId: String = "",
    val vimeoHash: String = "",
    val order: Int = 0 // Para mantener el orden de las lecciones
)

