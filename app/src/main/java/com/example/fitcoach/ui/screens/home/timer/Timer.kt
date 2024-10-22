package com.example.fitcoach.ui.screens.home.timer

import android.annotation.SuppressLint
import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fitcoach.ui.screens.home.AccentOrange
import com.example.fitcoach.ui.screens.home.DarkBlueDark
import com.example.fitcoach.ui.screens.home.CommonBottomBar
import androidx.lifecycle.viewmodel.compose.viewModel // Este es el import crucial


@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    TimerScreen(navController = rememberNavController())
}
@Composable
fun TimerScreen(
    navController: NavHostController,
    viewModel: TimerViewModel = viewModel()
) {
    val hours by viewModel.hours.collectAsState()
    val minutes by viewModel.minutes.collectAsState()
    val seconds by viewModel.seconds.collectAsState()
    val isActive by viewModel.isActive.collectAsState()

    Scaffold(
        bottomBar = { CommonBottomBar(navController, isSystemInDarkTheme()) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlueDark)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TimerDisplay(hours, minutes, seconds, isActive, viewModel::updateHours, viewModel::updateMinutes, viewModel::updateSeconds)
            Spacer(modifier = Modifier.height(32.dp))
            TimerControls(isActive, viewModel::toggleTimer, viewModel::resetTimer)
        }
    }
}

@Composable
fun TimerDisplay(
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
                TimeSeparator()
                TimeSelector(minutes, onMinutesChange, 59)
                TimeSeparator()
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
            Icon(Icons.Default.KeyboardArrowUp, "Increase", tint = Color.White)
        }
        Text(
            text = String.format("%02d", value),
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        TimerButton(onClick = { if (value > 0) onValueChange(value - 1) }) {
            Icon(Icons.Default.KeyboardArrowDown, "Decrease", tint = Color.White)
        }
    }
}

@Composable
fun TimeSeparator() {
    Text(":", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun TimerButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    IconButton(onClick = onClick, content = content)
}

@Composable
fun TimerControls(isActive: Boolean, onToggle: () -> Unit, onReset: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onToggle,
            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
            modifier = Modifier.size(width = 200.dp, height = 50.dp)
        ) {
            Text(if (isActive) "PAUSE" else "START", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onReset,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            modifier = Modifier.size(width = 200.dp, height = 50.dp)
        ) {
            Text("RESET", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}