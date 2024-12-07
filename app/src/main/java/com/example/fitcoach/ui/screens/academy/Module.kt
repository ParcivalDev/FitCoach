package com.example.fitcoach.ui.screens.academy

// Module.kt
data class Module(
    val id: String = "",
    val name: String = "",
    val order: Int = 0,  // Para mantener el orden de los módulos
    val lessonCount: Int = 0
)