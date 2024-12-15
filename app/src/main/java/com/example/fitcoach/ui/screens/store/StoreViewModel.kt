package com.example.fitcoach.ui.screens.store

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ViewModel de la pantalla de la tienda que se encarga de obtener los productos de la base de datos
class StoreViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Carga los productos de la base de datos
    init {
        loadProducts()
    }

    private fun loadProducts() {
        _isLoading.value = true
        db.collection("store") // Selecciona la colección de la base de datos
            .get()
            .addOnSuccessListener { documents -> // Obtiene los documentos de la colección y los mapea a objetos de tipo Product
                _products.value = documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)
                }
                _isLoading.value = false
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }
}