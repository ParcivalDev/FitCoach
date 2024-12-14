package com.example.fitcoach.ui.screens.chat

// Clase que representa un mensaje en la conversación del chat
data class ChatMessage(
    val content: String, // Contenido del mensaje
    val fromUser: Boolean, // Indica si el mensaje fue enviado por el usuario
    val timestamp: Long = System.currentTimeMillis() // Marca de tiempo del mensaje
)