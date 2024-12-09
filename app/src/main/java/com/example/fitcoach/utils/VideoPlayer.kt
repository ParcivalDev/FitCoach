package com.example.fitcoach.utils

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun VideoPlayer(
    videoId: String, // ID del video en Vimeo
    videoHash: String = "", // Hash necesario para algunos videos
    onDismiss: () -> Unit
) {
    // Comprobamos si la configuración actual es en modo portrait
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Mostramos un diálogo con un reproductor de video de Vimeo
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true, // Permitimos cerrar el diálogo al pulsar el botón de atrás
            dismissOnClickOutside = true, // Permitimos cerrar el diálogo al pulsar fuera de él
            usePlatformDefaultWidth = false // No usamos el ancho por defecto de la plataforma
        )
    ) {
        // Nos permite obtener el tamaño máximo de la pantalla
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            // Calculamos el ancho del reproductor de video
            val width = if (!isPortrait) {
                (maxHeight * (16f / 9f)).coerceAtMost(maxWidth * 0.8f)
            } else {
                maxWidth * 0.9f // El ancho es del 90% en modo portrait
            }

            // Tarjeta que contiene el reproductor de video
            Card(
                modifier = Modifier
                    .width(width)
                    .aspectRatio(16f / 9f) // Relación de aspecto 16:9
                    .padding(if (isPortrait) 8.dp else 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    VimeoWebView(videoId, videoHash)
                }
            }
        }
    }
}

// WebView personalizado para reproducir videos de Vimeo
@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun VimeoWebView(videoId: String, videoHash: String = "") {
    // Función para obtener la URL del video de Vimeo.
    // Si el ejercicio tiene un hash, lo añadimos a la URL ya que en algunos casos es necesario.
    val videoUrl = if (videoHash.isNotEmpty()) {
        "https://player.vimeo.com/video/$videoId?h=$videoHash&badge=0&autopause=0&player_id=0&app_id=58479"
    } else {
        "https://player.vimeo.com/video/$videoId?badge=0&autopause=0&player_id=0&app_id=58479"
    }

    // WebView que carga el reproductor de video de Vimeo
    AndroidView(
        factory = { context ->
            // Configuramos el WebView
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    // Establecemos el ancho y alto del WebView a MATCH_PARENT para que ocupe todo el espacio disponible
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // Configuramos el WebViewClient para que no abra enlaces externos
                // Solo permitimos abrir enlaces que no sean del reproductor de Vimeo
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return request?.url?.toString()
                            ?.startsWith("https://player.vimeo.com") != true
                    }
                }
                settings.apply {
                    javaScriptEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    domStorageEnabled = true
                    allowContentAccess = true
                    allowFileAccess = true
                    mediaPlaybackRequiresUserGesture = false
                    setSupportMultipleWindows(true)
                }

                // Cargamos el reproductor de video de Vimeo
                // Creamos un HTML con un iframe que carga el video de Vimeo
                val html = """
                    <html>
                        <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <style>
                                body, html { 
                                    margin: 0;  
                                    padding: 0; 
                                    width: 100%; 
                                    height: 100%; 
                                    background-color: black;
                                }
                                iframe {
                                    position: absolute;
                                    top: 0;
                                    left: 0;
                                    width: 100%;
                                    height: 100%;
                                    border: none;
                                }
                            </style>
                        </head>
                        <body>
                            <iframe 
                                src="$videoUrl"
                                frameborder="0" 
                                allow="autoplay; fullscreen; picture-in-picture"
                                allowfullscreen>
                            </iframe>
                        </body>
                    </html>
                """.trimIndent()

                // Cargamos el HTML en el WebView
                loadDataWithBaseURL(
                    "https://player.vimeo.com",
                    html,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}