package com.example.fitcoach.ui.screens.timer


import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitcoach.ui.screens.home.components.CommonBottomBar
import com.example.fitcoach.ui.theme.DarkBlueDark


@Composable
fun TimerScreen(
    navController: NavHostController,
    vm: TimerViewModel = viewModel()
) {
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
            TimerDisplay(
                hours = vm.hours,
                minutes = vm.minutes,
                seconds = vm.seconds,
                isActive = vm.isActive,
                onHoursChange = { vm.updateHours(it) },
                onMinutesChange = { vm.updateMinutes(it) },
                onSecondsChange = { vm.updateSeconds(it) }
            )
            Spacer(modifier = Modifier.height(100.dp))
            TimerControls(
                isActive = vm.isActive,
                onToggle = { vm.toggleTimer() },
                onReset = { vm.resetTimer() }
            )
        }
    }
}

