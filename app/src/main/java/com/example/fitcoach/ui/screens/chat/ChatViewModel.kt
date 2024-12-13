package com.example.fitcoach.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val chatbot = FitCoachChatbot()
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    init {
        // Mensaje de bienvenida
        _messages.value = listOf(
            ChatMessage(
                content = "¡Hola! Soy el asistente de FitCoach. Puedo ayudarte a conocer mejor la app y resolver tus dudas sobre cualquier sección. ¿Qué te gustaría saber?",
                fromUser = false
            )
        )
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            // Añadir mensaje del usuario
            _messages.value += ChatMessage(message, fromUser = true)

            _isTyping.value = true

            // Simular tiempo de respuesta para que parezca más natural
            delay(1000)

            // Obtener y añadir respuesta del chatbot
            val response = chatbot.getResponse(message)
            _messages.value += ChatMessage(response, fromUser = false)

            _isTyping.value = false
        }
    }
}