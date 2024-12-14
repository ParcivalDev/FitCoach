package com.example.fitcoach.ui.screens.chat

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitcoach.R

// Esta función es la que se encarga de mostrar el diálogo de chat en la pantalla
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDialog(
    onDismiss: () -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    // Recuperamos los mensajes y el estado de si se está escribiendo o no
    val messages by viewModel.messages.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()
    var inputText by remember { mutableStateOf("") }

    // Detectar la orientación
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Preguntas frecuentes
    val questions = listOf(
        stringResource(R.string.necesito_ayuda),
        stringResource(R.string.que_ejercicios_hay),
        stringResource(R.string.que_es_la_academia),
        stringResource(R.string.como_usar_calendario),
        stringResource(R.string.que_hay_en_el_blog),
        stringResource(R.string.como_funciona_el_temporizador),
        )

    // Función que se encarga de enviar un mensaje al chat
    fun onUserInput(text: String) {
        if (text.isNotBlank()) { // Si el texto no está vacío
            viewModel.sendMessage(text)
            inputText = "" // Limpiar el campo de texto
        }
    }

    // Diálogo de chat
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true, // Cerrar al pulsar atrás
            dismissOnClickOutside = true, // Cerrar al pulsar fuera del diálogo
            usePlatformDefaultWidth = false // No usa el ancho por defecto
        )
    ) {
        // Contenido del diálogo
        Card(
            modifier = Modifier
                // En portrait ocupa más espacio que en landscape
                .fillMaxWidth(if (isPortrait) 0.9f else 0.8f)
                .fillMaxHeight(if (isPortrait) 0.8f else 0.9f)
                .padding(if (isPortrait) 16.dp else 8.dp), // Añadir padding respecto al borde de la pantalla
            shape = RoundedCornerShape(16.dp)
        ) {
            // Columna con el contenido del chat
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Barra superior del chat con el título y el botón de cerrar
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.asistente_fitcoach),
                            fontSize = if (isPortrait) 20.sp else 18.sp
                        )
                    },
                    actions = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Rounded.Close, stringResource(R.string.limpiar_chat))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
                // Lista de preguntas frecuentes en una lista horizontal con scroll
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = if (isPortrait) 16.dp else 12.dp,
                            vertical = 8.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Lista de preguntas frecuentes
                    items(questions) { question ->
                        // Botón con la pregunta
                        Surface(
                            modifier = Modifier.clickable {
                                onUserInput(question) // Enviar la pregunta al chat
                            },
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = question,
                                modifier = Modifier.padding(
                                    horizontal = if (isPortrait) 12.dp else 8.dp,
                                    vertical = 8.dp
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // Lista de mensajes en el chat
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = if (isPortrait) 16.dp else 12.dp),
                    reverseLayout = true // Para que los mensajes se muestren de abajo a arriba
                ) {
                    if (isTyping) {
                        item {
                            TypingIndicator() // Mostrar el indicador de que se está escribiendo
                        }
                    }

                    // Mostrar los mensajes en orden inverso
                    items(messages.asReversed()) { message ->
                        ChatMessageItem(message)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Campo de entrada de texto para enviar mensajes
                ChatInput(
                    value = inputText,
                    onValueChange = { inputText = it },
                    onSend = {
                        onUserInput(inputText)
                    },
                    isPortrait = isPortrait
                )
            }
        }
    }
}

// Componente que muestra un mensaje en el chat
@Composable
fun ChatMessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        // Alinea mensajes del usuario a la derecha y del bot a la izquierda
        horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 300.dp),
            // Colores de la tarjeta según si el mensaje es del usuario o del bot
            colors = CardDefaults.cardColors(
                containerColor = if (message.fromUser)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            // Contenido del mensaje
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = if (message.fromUser)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Componente que muestra un indicador de que se está escribiendo
@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .padding(14.dp)
            .width(64.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Mostrar tres puntos animados
        repeat(3) {
            var offset by remember { mutableFloatStateOf(0f) }

            LaunchedEffect(Unit) {
                animate(
                    initialValue = 0f,
                    targetValue = 10f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(500),
                        repeatMode = RepeatMode.Reverse
                    )
                ) { value, _ -> offset = value }
            }

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .offset(y = -offset.dp) // sirve para mover el punto hacia arriba y abajo en el eje Y y así
                    .background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            )
        }
    }
}

// Campo de entrada de texto para enviar mensajes
@Composable
fun ChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isPortrait: Boolean
) {
    // Fila con el campo de texto y el botón de enviar
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(if (isPortrait) 16.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    stringResource(R.string.escribe_un_mensaje),
                    fontSize = if (isPortrait) 14.sp else 12.sp
                )
            },
            textStyle = LocalTextStyle.current.copy(
                fontSize = if (isPortrait) 14.sp else 12.sp
            ),
            maxLines = 3
        )

        IconButton(
            onClick = onSend,
            enabled = value.isNotBlank(),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.Send,
                contentDescription = stringResource(R.string.icon_enviar),
                tint = if (value.isNotBlank())
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                modifier = Modifier.size(30.dp)
            )
        }
    }
}