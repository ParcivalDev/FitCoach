package com.example.fitcoach.ui.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitcoach.ui.theme.AccentOrange

@Composable
fun Temporizador(
    hours: Int, minutes: Int, seconds: Int, isActive: Boolean,
    onHoursChange: (Int) -> Unit, onMinutesChange: (Int) -> Unit, onSecondsChange: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(280.dp)
            .clip(CircleShape)
            .background(Color(0xFF1E2A4A))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (!isActive) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeSelector(hours, onHoursChange, 23)
                Separador()
                TimeSelector(minutes, onMinutesChange, 59)
                Separador()
                TimeSelector(seconds, onSecondsChange, 59)
            }
        } else {
            Text(
                text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TimeSelector(value: Int, onValueChange: (Int) -> Unit, maxValue: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimerButton(onClick = { if (value < maxValue) onValueChange(value + 1) }) {
            Icon(Icons.Default.KeyboardArrowUp, "", tint = Color.White)
        }
        Text(
            text = String.format("%02d", value),
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        TimerButton(onClick = { if (value > 0) onValueChange(value - 1) }) {
            Icon(Icons.Default.KeyboardArrowDown, "", tint = Color.White)
        }
    }
}

@Composable
fun Separador() {
    Text(":", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun TimerButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    IconButton(onClick = onClick, content = content)
}

@Composable
fun TimerControls(isActive: Boolean, onToggle: () -> Unit, onReset: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(
            onClick = onToggle,
            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
            modifier = Modifier.size(width = 150.dp, height = 50.dp)
        ) {
            Text(if (isActive) "PAUSE" else "START", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(32.dp))
        Button(
            onClick = onReset,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            modifier = Modifier.size(width = 150.dp, height = 50.dp)
        ) {
            Text("RESET", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}