package com.example.fitcoach.ui.screens.store

// Clase que representa un producto en la tienda
data class Product(
    val title: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val isFree: Boolean = false,  // Para marcar los productos gratuitos
    val url: String = "",  // URL para la compra
)
