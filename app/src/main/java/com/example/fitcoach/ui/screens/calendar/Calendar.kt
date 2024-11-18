package com.example.fitcoach.ui.screens.calendar


import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fitcoach.ui.screens.home.components.CommonBottomBar
import com.example.fitcoach.ui.theme.FitCoachTheme
import java.time.YearMonth



@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CalendarScreenPreview() {
    FitCoachTheme {
        CalendarScreen(
            navController = rememberNavController(),
            vm = CalendarViewModel()
        )
    }
}

//
//CALENDARIO DEBERÍA TENER MARCADO EL DÍA ACTUAL
//

// Función principal que muestra la pantalla del calendario
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavHostController,
    vm: CalendarViewModel,  // ViewModel que maneja la lógica y estado
) {
    // Scaffold proporciona la estructura básica de la pantalla
    Scaffold(
        bottomBar = {
            // Barra inferior con botones de navegación
            CommonBottomBar(navController, isSystemInDarkTheme())
        }
    ) { padding ->
        // Contenido principal en una columna
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Componente para seleccionar el mes
            MonthSelector(vm)

            // Cabecera con los días de la semana
            WeekDaysHeader()

            // Grid del calendario con los días
            CalendarGrid(vm)

            // Área para mostrar y añadir notas
            NotesArea(vm)
        }
    }

    // Dialog para añadir notas (solo se muestra cuando showDialog es true)
    if (vm.showDialog) {
        AddNoteDialog(vm)
    }
}

// Componente que muestra el selector de mes con flechas
@Composable
fun MonthSelector(viewModel: CalendarViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón para mes anterior
        IconButton(onClick = { viewModel.onMonthChange(false) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Mes anterior")
        }

        // Texto con el mes y año actual
        Text(
            text = "${viewModel.currentMonth.month} ${viewModel.currentMonth.year}",
            style = MaterialTheme.typography.titleLarge
        )

        // Botón para mes siguiente
        IconButton(onClick = { viewModel.onMonthChange(true) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Mes siguiente")
        }
    }
}

// Muestra los días de la semana en la parte superior
@Composable
fun WeekDaysHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Grid que muestra los días del mes
@Composable
fun CalendarGrid(viewModel: CalendarViewModel) {
    val days = getDaysInMonth(viewModel.currentMonth)

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(days) { date ->
            DayCell(
                date = date,
                isSelected = date == viewModel.selectedDate,
                workoutNote = date?.let { viewModel.workoutNotes[it] },
                onDateClick = { date?.let { viewModel.onDateSelect(it) } }
            )
        }
    }
}

// Celda individual para cada día del mes
@Composable
fun DayCell(
    date: LocalDate?,           // Fecha del día (null para días vacíos)
    isSelected: Boolean,        // Si está seleccionado
    workoutNote: WorkoutNote?,  // Nota de entreno si existe
    onDateClick: () -> Unit     // Función al hacer click
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)  // Hace que la celda sea cuadrada
            .padding(2.dp)
            .clickable(enabled = date != null, onClick = onDateClick),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                workoutNote != null -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        if (date != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Número del día
                Text(text = date.dayOfMonth.toString())
                // Emoji si hay nota
                if (workoutNote != null) {
                    Text(text = workoutNote.rating.emoji)
                }
            }
        }
    }
}

// Área que muestra las notas del día seleccionado
@Composable
fun NotesArea(viewModel: CalendarViewModel) {
    viewModel.selectedDate?.let { date ->
        Column(modifier = Modifier.padding(16.dp)) {
            // Fecha seleccionada
            Text(
                text = "Día ${date.dayOfMonth}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Muestra nota existente o botón para añadir
            val note = viewModel.workoutNotes[date]
            if (note != null) {
                NoteCard(note, onEditClick = viewModel::onShowDialog)
            } else {
                Button(
                    onClick = viewModel::onShowDialog,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir nota de entreno")
                }
            }
        }
    }
}

// Componente que muestra una nota existente
@Composable
fun NoteCard(
    note: WorkoutNote,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = note.rating.emoji, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = note.note)
        }
    }
}

// Dialog para añadir o editar una nota
@Composable
fun AddNoteDialog(viewModel: CalendarViewModel) {
    var noteText by remember { mutableStateOf("") }
    var selectedRating by remember { mutableStateOf(WorkoutRating.GOOD) }

    AlertDialog(
        onDismissRequest = viewModel::onHideDialog,
        title = { Text("Añadir nota de entreno") },
        text = {
            Column {
                // Campo para escribir la nota
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("¿Qué tal el entreno?") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 10
                )

                // Selector de rating con emojis
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WorkoutRating.entries.forEach { rating ->
                        FilterChip(
                            selected = selectedRating == rating,
                            onClick = { selectedRating = rating },
                            label = { Text(rating.emoji) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.selectedDate?.let {
                        viewModel.onSaveNote(it, noteText, selectedRating)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = viewModel::onHideDialog) {
                Text("Cancelar")
            }
        }
    )
}

// Función auxiliar para obtener los días del mes
private fun getDaysInMonth(yearMonth: YearMonth): List<LocalDate?> {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    return buildList {
        // Días vacíos al inicio para alinear con el día de la semana correcto
        repeat(firstDayOfWeek) { add(null) }
        // Días del mes
        for (day in 1..daysInMonth) {
            add(yearMonth.atDay(day))
        }
    }
}

data class WorkoutNote(
    val note: String,
    val rating: WorkoutRating
)

enum class WorkoutRating(val emoji: String) {
    EXCELLENT("🔥"),
    GOOD("💪"),
    OKAY("😐"),
    BAD("😫")
}