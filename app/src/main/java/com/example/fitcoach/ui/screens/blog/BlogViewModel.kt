package com.example.fitcoach.ui.screens.blog

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//Contiene la lógica de que maneja la obtención y ordenamiento de los artículos del blog
class BlogViewModel : ViewModel() {
    // Instancia de Firestore para acceder a la base de datos
    private val db = FirebaseFirestore.getInstance()

    // Para manejar la lista de artículos mostrando cambios en tiempo real
    // Se inicializa con una lista vacía de artículos
    private val _articles = MutableStateFlow<List<BlogArticle>>(emptyList())
    val articles: StateFlow<List<BlogArticle>> = _articles.asStateFlow()

    // Indica si se está cargando la lista de artículos
    private val _isLoading = MutableStateFlow(true) // privado y mutable solo en esta clase
    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow() // público y de solo lectura para evitar cambios externos

    // Guarda la categoría seleccionada por el usuario
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Al inicializar el ViewModel, se cargan los artículos
    init {
        loadArticles()
    }

    // Método para seleccionar una categoría
    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    // Método para cargar los artículos desde Firestore
    private fun loadArticles() {
        db.collection("blog_posts")
            .get()
            .addOnSuccessListener { documents ->
                // Mapea los documentos a objetos BlogArticle
                _articles.value = documents.mapNotNull { doc ->
                    doc.toObject(BlogArticle::class.java)
                }.sortedByDescending { article ->
                    // Ordena los artículos por fecha de publicación
                    convertStringToDate(article.date)
                }
                _isLoading.value = false
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }

    // Convierte una cadena de fecha en un objeto Date para ordenar los artículos
    private fun convertStringToDate(dateString: String): Date {
        return try {
            val formatter = SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ) // Locale.getDefault() para usar el formato de fecha del dispositivo
            formatter.parse(dateString) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }
}