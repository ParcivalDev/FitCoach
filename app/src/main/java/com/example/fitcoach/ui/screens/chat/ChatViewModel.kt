package com.example.fitcoach.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel para la pantalla de chat que se encarga de gestionar los mensajes del chatbot
class ChatViewModel : ViewModel() {
    // Instancia del chatbot
    private val chatbot = FitCoachChatbot()

    // Lista de mensajes del chat
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Estado que indica si el chatbot está escribiendo
    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    init {
        // Mensaje de bienvenida
        _messages.value = listOf(
            ChatMessage(
                content = "¡Hola! Soy el asistente de FitCoach. Puedo ayudarte a conocer mejor la app y resolver tus dudas sobre cualquier sección. ¿Qué te gustaría saber?",
                fromUser = false // El mensaje es del chatbot
            )
        )
    }

    // Método para enviar un mensaje al chatbot
    fun sendMessage(message: String) {
        // Usamos viewModelScope para lanzar una corrutina
        // que tiene la ventaja de que se cancela automáticamente
        // cuando el ViewModel es destruido evitando problemas de memoria
        viewModelScope.launch {
            // Añadir mensaje del usuario a la lista de mensajes
            _messages.value += ChatMessage(message, fromUser = true)

            // Indicar que el chatbot está escribiendo
            _isTyping.value = true

            // Simular tiempo de respuesta
            delay(1000)

            // Obtener y añadir respuesta del chatbot a la lista de mensajes
            val response = chatbot.getResponse(message)
            _messages.value += ChatMessage(response, fromUser = false)

            _isTyping.value = false
        }
    }
}
