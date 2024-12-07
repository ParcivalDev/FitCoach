package com.example.fitcoach.ui.screens.calendar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fitcoach.R
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

// Función principal que muestra la pantalla del calendario
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun CalendarScreen(
    navController: NavHostController,
    vm: CalendarViewModel,  // ViewModel que maneja la lógica y estado
) {
    // Contexto actual para manejar la orientación
    val context = LocalContext.current

    // Bloquea la orientación para que sea siempre vertical
    DisposableEffect(Unit) {
        val activity = context as Activity
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Restaura la orientación original al salir
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }

    // Scaffold proporciona la estructura básica de la pantalla
    Scaffold(
        bottomBar = {
            // Barra inferior con botones de navegación
            CommonBottomBar(navController, isSystemInDarkTheme(), isPortrait = true)
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

    // Mostrar cargando si isLoading es true
    if (vm.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    // Mostrar error si existe
    // Con .let { } se ejecuta el bloque solo si errorMessage no es null
    // Este dialog se muestra cuando hay un error en la carga de notas o al guardar
    vm.errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = { vm.clearError() },
            title = { Text(stringResource(R.string.error)) },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { vm.clearError() }) {
                    Text(stringResource(R.string.aceptar))
                }
            }
        )
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
            Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.mes_anterior))
        }

        // Texto con el mes y año actual
        Text(
            text = "${viewModel.currentMonth.month} ${viewModel.currentMonth.year}",
            style = MaterialTheme.typography.titleLarge
        )

        // Botón para mes siguiente
        IconButton(onClick = { viewModel.onMonthChange(true) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, stringResource(R.string.mes_siguiente))
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
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Grid que muestra los días del mes
@Composable
fun CalendarGrid(viewModel: CalendarViewModel) {
    val days = getDaysInMonth(viewModel.currentMonth) // Obtener los días del mes actual

    LazyVerticalGrid(
        columns = GridCells.Fixed(7), // 7 columnas para los días de la semana
        modifier = Modifier.fillMaxWidth()
    ) {
        items(days) { date ->
            DayCell( // Celda individual para cada día
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
    // Obtener la fecha actual
    val hoy = date?.equals(LocalDate.now()) ?: false

    Card(
        modifier = Modifier
            .aspectRatio(1f)  // Hace que la celda sea cuadrada
            .padding(2.dp)
            .clickable(
                enabled = date != null,
                onClick = onDateClick
            ), // Hacer clickeable solo si hay fecha
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                workoutNote != null -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        // Añadir borde solo para el día actual
        border = if (hoy) BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.primary
        ) else null
    ) {
        if (date != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Número del día
                Text(
                    text = date.dayOfMonth.toString(),
                    color = when {
                        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                        workoutNote != null -> MaterialTheme.colorScheme.onSecondaryContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
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
    viewModel.selectedDate?.let { date -> // Mostrar solo si hay fecha seleccionada
        Column(modifier = Modifier.padding(16.dp)) {
            // Fecha seleccionada
            Text(
                text = stringResource(R.string.dia_num, date.dayOfMonth),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Muestra nota existente o botón para añadir
            val note = viewModel.workoutNotes[date]
            if (note != null) {
                NoteCard(note, onEditClick = viewModel::onShowDialog, onDeleteClick = {
                    viewModel.onDeleteNote(date)
                })
            } else {
                Button(
                    onClick = viewModel::onShowDialog,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(stringResource(R.string.anhadir_nota_entreno))
                }
            }
        }
    }
}

// Componente que muestra una nota existente
@Composable
fun NoteCard(
    note: WorkoutNote,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = note.rating.emoji,
                    style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = note.note)
            }

            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = stringResource(R.string.borrar_nota),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// Dialog para añadir o editar una nota
@Composable
fun AddNoteDialog(viewModel: CalendarViewModel) {
    // Obtener la nota existente si la hay
    val existingNote = viewModel.selectedDate?.let { viewModel.workoutNotes[it] }

    var noteText by remember { mutableStateOf(existingNote?.note ?: "") }
    var selectedRating by remember { mutableStateOf(existingNote?.rating ?: WorkoutRating.GOOD) }

    AlertDialog(
        onDismissRequest = viewModel::onHideDialog,
        title = {
            Text(
                if (existingNote != null) stringResource(R.string.editar_nota) else stringResource(
                    R.string.anhadir_nota
                )
            )
        },
        text = {
            Column {
                // Campo para escribir la nota
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text(stringResource(R.string.que_tal_entreno)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 10
                )

                // Selector de emoji
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Primera fila: primeros 3 ratings
                        WorkoutRating.entries.take(3).forEach { rating ->
                            FilterChip(
                                selected = selectedRating == rating,
                                onClick = { selectedRating = rating },
                                label = { Text(rating.emoji) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Segunda fila: últimos 3 ratings
                        WorkoutRating.entries.drop(3).forEach { rating ->
                            FilterChip(
                                selected = selectedRating == rating,
                                onClick = { selectedRating = rating },
                                label = { Text(rating.emoji) }
                            )
                        }
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
                Text(stringResource(R.string.guardar))
            }
        },
        dismissButton = {
            TextButton(onClick = viewModel::onHideDialog) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}

// Función para obtener los días del mes
private fun getDaysInMonth(yearMonth: YearMonth): List<LocalDate?> {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value - 1

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
    GOOD("👍"),
    OKAY("😐"),
    TIRED("🪫"),
    BAD("👎"),
    TERRIBLE("🤒")
}

