package com.example.fitcoach.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fitcoach.R

// Diálogo que muestra la información de contacto del entrenador
@Composable
fun ContactDialog(
    onDismiss: () -> Unit, // Función que se ejecuta al cerrar el diálogo
    onWhatsAppClick: () -> Unit, // Función que se ejecuta al hacer clic en el botón de WhatsApp
    onEmailClick: () -> Unit // Función que se ejecuta al hacer clic en el botón de Email
) {
    Dialog(onDismissRequest = onDismiss) {
        // Tarjeta que contiene la información de contacto
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.SupportAgent,
                    contentDescription = stringResource(R.string.icon_ayuda),
                    Modifier.padding(bottom = 8.dp)
                )


                Text(
                    stringResource(R.string.text_contact_dialog),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()

                )
                Text(
                    stringResource(R.string.subtext_contact_dialog),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(26.dp))

                // Columna que contiene la información de contacto
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = stringResource(R.string.email_de_contacto)
                        )
                        Text(stringResource(R.string.email_entrenador))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = stringResource(R.string.telf_de_contacto)
                        )
                        Text(stringResource(R.string.num_entrenador))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Fila que contiene los botones de WhatsApp y Email
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { onWhatsAppClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00bb2d)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.whatsapp), color = Color.White)
                    }
                    Button(
                        onClick = { onEmailClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1467C7)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.email), color = Color.White)
                    }
                }
            }
        }
    }
}

