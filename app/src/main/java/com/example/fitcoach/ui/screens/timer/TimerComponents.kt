package com.example.fitcoach.ui.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitcoach.R
import com.example.fitcoach.ui.theme.AccentOrange
import com.example.fitcoach.ui.theme.BackgroundLight
import com.example.fitcoach.ui.theme.CardDark
import com.example.fitcoach.ui.theme.CardLight
import com.example.fitcoach.ui.theme.DarkBlueDark
import java.util.Locale

@Preview
@Composable
fun TimerScreenPreview() {
    TimerControls(false, {}, {})
}


@Composable
fun Temporizador(
    isDarkTheme: Boolean,
    hours: Int,
    minutes: Int,
    seconds: Int,
    isActive: Boolean,
    onHoursChange: (Int) -> Unit,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit
) {
    // Cambiamos los colores según el tema
    val boxColor = if (isDarkTheme) DarkBlueDark else BackgroundLight
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(1f) // Mantenemos el aspecto circular
            .clip(CircleShape)
            .background(boxColor)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Si el temporizador no está activo, se muestran los selectores de tiempo
        if (!isActive) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeSelector(
                    hours,
                    onHoursChange,
                    23,
                    isDarkTheme,
                    textColor
                ) // Selector de horas (0-23)
                Separador(textColor) // Separador :
                TimeSelector(minutes, onMinutesChange, 59, isDarkTheme, textColor)
                Separador(textColor)
                TimeSelector(seconds, onSecondsChange, 59, isDarkTheme, textColor)
            }
        } else { // Si el temporizador está activo, se muestra el tiempo restante
            Text(
                text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d:%02d", // Formato HH:MM:SS
                    hours,
                    minutes,
                    seconds
                ),
                color = textColor,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TimeSelector(
    value: Int,
    onValueChange: (Int) -> Unit,
    maxValue: Int,
    isDarkTheme: Boolean,
    textColor: Color
) {
    // Configuramos el estado inicial de la lista
    // value: es el número que queremos mostrar (ejemplo: 5)
    // maxValue: es el número máximo permitido (ejemplo: para horas sería 23)
    // Sumamos (maxValue + 1) para tener un ciclo completo de números extras
    // y así poder hacer scroll infinito hacia arriba y abajo
    // Ejemplo: Si value=5 y maxValue=23:
    // initialFirstVisibleItemIndex = 5 + 24 = 29
    // Así cuando usemos el módulo (29 % 24 = 5) nos dará el número que queremos
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = value + (maxValue + 1)
    )

    // Box que contiene el selector de tiempo
    Box(
        modifier = Modifier
            .height(160.dp)
            .width(80.dp),
        contentAlignment = Alignment.Center
    ) {
        val resaltado = if (isDarkTheme) Color(0xFF2A3A5A) else Color.LightGray

        // Resaltado del número seleccionado
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(50.dp)
                .background(resaltado, RoundedCornerShape(12.dp))

        )
        //Convierte la posición actual a un número de nuesro rango ya que la lista es infinita
        // y la posición actual puede ser mayor al rango de números
        val currentIndex = remember {
            derivedStateOf {
                listState.firstVisibleItemIndex % (maxValue + 1)
            }
        }

        // Lista de números con scroll infinito
        LazyColumn(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            contentPadding = PaddingValues(vertical = 55.dp)
        ) {
            // Creamos una lista infinita de números
            items(count = Int.MAX_VALUE) { index ->
                //Obtenemos el valor real de nuestra selección
                val numero = index % (maxValue + 1)

                // Box para cada número
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val estaSeleccionado = currentIndex.value == numero

                    Text(
                        text = String.format(Locale.getDefault(), "%02d", numero),
                        // Si está en el centro es blanco, si no: blanco con transparencia
                        color = if (estaSeleccionado) textColor else textColor.copy(alpha = 0.3f),
                        // Si está en el centro es más grande
                        fontSize = if (estaSeleccionado) 32.sp else 26.sp
                    )
                }
            }
        }

        // Cuando se detiene el scroll, actualizamos el valor
        LaunchedEffect(currentIndex.value) {
            onValueChange(currentIndex.value)
        }
    }
}

@Composable
fun Separador(textColor: Color) {
    Text(":", color = textColor, fontSize = 48.sp, fontWeight = FontWeight.Bold)
}


@Composable
fun TimerControls(isActive: Boolean, onToggle: () -> Unit, onReset: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        IconButton(
            onClick = onToggle, // Función para iniciar/pausar
            colors = IconButtonDefaults.iconButtonColors(containerColor = AccentOrange),
            modifier = Modifier.size(
                // Más ancho cuando es Play y más pequeño cuando es Pause
                width = if (isActive) 64.dp else 120.dp,
                height = 64.dp
            )
        ) {
            Icon(
                // Cambia el icono según el estado
                if (isActive) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                stringResource(R.string.icono_de_play_pause),
                tint = Color.White,
                modifier = if (isActive) Modifier.size(32.dp) else Modifier.size(40.dp)
            )
        }

        // Botón de Stop solo visible cuando está el temporizador activo
        if (isActive) {
            Spacer(modifier = Modifier.width(32.dp))
            IconButton(
                onClick = onReset, // Función para detener
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    Icons.Rounded.Stop,
                    stringResource(R.string.icono_de_detener),
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

// Botones de selección rápida
@Composable
fun BotonSeleccionTiempo(
    texto: String,
    onClick: () -> Unit,
    isDarkTheme: Boolean
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDarkTheme) CardDark else CardLight
        ),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .height(50.dp)
            .width(90.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = texto,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDarkTheme) Color.White else Color.Black
        )
    }
}

