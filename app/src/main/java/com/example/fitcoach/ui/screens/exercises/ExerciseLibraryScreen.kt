package com.example.fitcoach.ui.screens.exercises

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fitcoach.R
import com.example.fitcoach.ui.screens.home.HomeViewModel
import com.example.fitcoach.ui.screens.home.components.CommonBottomBar
import com.example.fitcoach.ui.theme.BackgroundDark
import com.example.fitcoach.ui.theme.BackgroundLight
import com.example.fitcoach.ui.theme.CardDark
import com.example.fitcoach.ui.theme.CardLight
import com.example.fitcoach.utils.VideoPlayer
import kotlinx.coroutines.launch

// Pantalla de la biblioteca de ejercicios
// Muestra una lista de ejercicios filtrada por grupo muscular y búsqueda
@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLibraryScreen(
    navController: NavHostController,
    viewModel: ExerciseLibraryViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(), // Necesitamos el viewmodel de Home para obtener los grupos musculares iniciales
    initialMuscle: String? = null
) {
    // Ejercicio seleccionado para mostrar en un dialog
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    // Texto actual de la búsqueda
    val searchQuery by viewModel.searchQuery.collectAsState()
    // Lista de ejercicios filtrada por búsqueda
    val filteredExercises by viewModel.filteredExercises.collectAsState()

    // Configuración actual del dispositivo para determinar si está en modo portrait o landscape
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Estado del menú lateral
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // CoroutineScope para manejar la apertura y cierre del menú
    val scope = rememberCoroutineScope()

    // Determinar si el tema actual es oscuro
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight

    val exercises by viewModel.exercises.collectAsState() // Lista de ejercicios
    val selectedMuscleGroup by viewModel.selectedMuscleGroup.collectAsState() // Grupo muscular seleccionado
    val isLoading by viewModel.isLoading.collectAsState() // Estado de carga
    val error by viewModel.error.collectAsState() // Error

    // Se ejecuta cuando se lanza la pantalla y al cambiar el grupo muscular seleccionado
    LaunchedEffect(initialMuscle) {
        viewModel.initialize(initialMuscle)
    }

    // Menú lateral con los grupos musculares
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = backgroundColor) {
                Spacer(modifier = Modifier.height(12.dp))
                Text( // Título del menú
                    stringResource(R.string.grupos_musculares),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(20.dp)
                )
                HorizontalDivider()

                LazyColumn( // Lista de grupos musculares
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = if (isPortrait) 12.dp else 16.dp),
                    contentPadding = PaddingValues(vertical = 10.dp), // Padding exterior
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre elementos
                ) {
                    // Por cada grupo muscular, mostramos un item en la lista
                    items(homeViewModel.exercises) { exercise ->
                        NavigationDrawerItem(
                            label = { Text(exercise.name) }, // Nombre del grupo muscular
                            selected = selectedMuscleGroup == exercise.name, // Si está seleccionado
                            // Al hacer click, seleccionamos el grupo muscular y cerramos el menú
                            onClick = {
                                viewModel.selectMuscleGroup(exercise.name)
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    ) {
        // Contenido principal de la pantalla con la lista de ejercicios
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopAppBar( // Barra superior con el título y el botón para abrir el menú
                        // El título es el grupo muscular seleccionado o "Biblioteca de Ejercicios" si no hay ninguno
                        title = {
                            Text(
                                selectedMuscleGroup
                                    ?: stringResource(R.string.biblioteca_de_ejercicios)
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Rounded.Menu, stringResource(R.string.icon_abrir_menu))
                            }
                        },
                        // Colores de la barra superior según el tema
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = cardColor)
                    )
                },
                // BottomBar común a todas las pantallas
                bottomBar = { CommonBottomBar(navController, isDarkTheme, isPortrait = isPortrait) },
                containerColor = backgroundColor
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // Contenido de la pantalla según el estado de carga puede ser error, lista de ejercicios o mensaje de no hay ejercicios
                    when {
                        isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        error != null -> Text(
                            text = stringResource(
                                R.string.error_al_cargar_los_ejercicios,
                                error.toString()
                            ),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                        // Si no hay ejercicios y hay un grupo muscular seleccionado, mostramos un mensaje
                        exercises.isEmpty() && selectedMuscleGroup != null -> Text(
                            text = stringResource(R.string.sin_ejercicios),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                        //else para mostrar la lista de ejercicios y la barra de búsqueda
                        else ->
                            Column {
                                SearchBar( // Barra de búsqueda
                                    query = searchQuery,
                                    onQueryChange = { viewModel.onSearchQueryChange(it) }
                                )
                                // Lista de ejercicios
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    contentPadding = PaddingValues(bottom = 10.dp), // Padding en la parte inferior de la lista
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(filteredExercises) { exercise -> // Por cada ejercicio, mostramos un item en la lista
                                        ListItem(
                                            headlineContent = {
                                                Text(
                                                    text = exercise.name, // Nombre del ejercicio
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            },
                                            colors = ListItemDefaults.colors(
                                                containerColor = cardColor,
                                            ),
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable(enabled = exercise.vimeoId.isNotEmpty()) {
                                                    selectedExercise = exercise
                                                },
                                            trailingContent = {
                                                if (exercise.vimeoId.isNotEmpty()) { // Si tiene un video, mostramos el icono de play
                                                    Icon(
                                                        Icons.Rounded.PlayCircle,
                                                        contentDescription = stringResource(R.string.icon_play_ejercicio),
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                    }
                }
            }

            // Dialog para mostrar el video del ejercicio seleccionado
            if (selectedExercise != null) {
                VideoPlayer(
                    videoId = selectedExercise!!.vimeoId,
                    videoHash = selectedExercise!!.vimeoHash,
                    onDismiss = { selectedExercise = null }
                )
            }
        }
    }
}


// Barra de búsqueda para filtrar los ejercicios
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField( // TextField con el icono de búsqueda
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text(stringResource(R.string.buscar_ejercicio)) },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(R.string.icon_buscar)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline
        )
    )
}