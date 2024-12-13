package com.example.fitcoach.ui.screens.blog

// Define la estructura de un artículo del blog
data class BlogArticle(
    val title: String = "", // Título del artículo
    val text: String = "", // Parte del texto del artículo
    val date: String = "", // Fecha de publicación 10/10/2021
    val categories: List<String> = emptyList(), // Lista de categorías a las que pertenece el artículo
    val url: String = "" // URL del artículo para navegar a la página
)