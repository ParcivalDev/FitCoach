package com.example.fitcoach.ui.screens.home.model

// Representa cada ejercicio en la biblioteca
data class ExerciseCategory(
    val name: String,          // Nombre del músculo/ejercicio
    val imageResource: Int     // ID del recurso de imagen
)

// Representa cada categoría (Entrenamiento, Academia, etc.)
data class Category(
    val name: String,          // Nombre de la categoría
    val imageResource: Int     // ID de la imagen de fondo
)

// Representa la entrada del blog
data class BlogPost(
    val title: String = "Blog",  // Título del post
    val imageResource: Int       // Imagen de fondo
)