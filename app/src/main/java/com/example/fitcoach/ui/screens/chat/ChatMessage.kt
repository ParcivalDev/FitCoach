package com.example.fitcoach.ui.screens.chat

data class ChatMessage(
    val content: String,
    val fromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)