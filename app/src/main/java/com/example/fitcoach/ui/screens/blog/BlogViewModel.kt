package com.example.fitcoach.ui.screens.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlogViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _articles = MutableStateFlow<List<BlogArticle>>(emptyList())
    val articles: StateFlow<List<BlogArticle>> = _articles.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        loadArticles()
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    private fun loadArticles() {
        db.collection("blog_posts")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                _articles.value = documents.mapNotNull { doc ->
                    doc.toObject(BlogArticle::class.java)
                }
                _isLoading.value = false
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }
}