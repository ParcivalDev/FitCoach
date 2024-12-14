package com.example.fitcoach.ui.screens.academy

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitcoach.R
import com.example.fitcoach.ui.screens.home.components.CommonBottomBar
import com.example.fitcoach.ui.theme.BackgroundDark
import com.example.fitcoach.ui.theme.BackgroundLight
import com.example.fitcoach.ui.theme.CardDark
import com.example.fitcoach.ui.theme.CardLight
import com.example.fitcoach.utils.VideoPlayer

// Pantalla de la academia
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademyScreen(
    navController: NavHostController,
    viewModel: AcademyViewModel
) {
    // Lección seleccionada
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }


    val modules by viewModel.modules.collectAsState()
    val lessons by viewModel.lessons.collectAsState()
    val selectedModuleId by viewModel.selectedModuleId.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Configuración de la pantalla y tema
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight

    // Estructura de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedModuleId?.let {
                            modules.find { it.id == selectedModuleId }?.name
                        } ?: "Academia",
                        fontSize = 20.sp
                    )
                },

                navigationIcon = {
                    if (selectedModuleId != null) {
                        IconButton(onClick = { viewModel.clearSelectedModule() }) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Volver")
                        }
                    } else {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Volver")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = cardColor)
            )
        },
        bottomBar = { CommonBottomBar(navController, isDarkTheme, isPortrait = true) },
        containerColor = backgroundColor
    ) { padding ->
        // Contenido de la pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Muestra un indicador de carga, un mensaje de error o la lista de módulos o lecciones
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                // Muestra un mensaje de error
                error != null -> {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Muestra la lista de módulos si no hay ninguno seleccionado
                selectedModuleId == null -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = if (isPortrait) 16.dp else 24.dp),
                        contentPadding = PaddingValues(
                            bottom = if (isPortrait) 80.dp else 64.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(modules) { module ->
                            ModuleCard( // Muestra un módulo
                                module = module,
                                onClick = { viewModel.selectModule(module.id) },
                                cardColor = cardColor
                            )
                        }
                    }
                }

                // Muestra la lista de lecciones si hay un módulo seleccionado
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = if (isPortrait) 16.dp else 24.dp),
                        contentPadding = PaddingValues(vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(lessons) { lesson ->
                            LessonItem(
                                lesson = lesson,
                                cardColor = cardColor,
                                onClick = { selectedLesson = lesson }
                            )
                        }
                    }
                }
            }

            // Muestra el reproductor de video si hay una lección seleccionada
            if (selectedLesson != null) {
                VideoPlayer(
                    videoId = selectedLesson!!.vimeoId,
                    videoHash = selectedLesson!!.vimeoHash,
                    onDismiss = { selectedLesson = null }
                )
            }
        }
    }
}

// Función que muestra una lección
@Composable
fun LessonItem(
    lesson: Lesson, // Datos de la lección
    cardColor: Color,
    onClick: () -> Unit // Función que se ejecuta al hacer clic en la lección  (muestra el video)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp) // Espaciado vertical entre lecciones
            .clickable(onClick = onClick)
    ) {
        ListItem(
            headlineContent = {
                Text(text = lesson.name) // Título de la lección
            },
            colors = ListItemDefaults.colors(
                containerColor = cardColor
            ),
            trailingContent = {
                // Muestra un icono de reproducción si la lección tiene un video
                if (lesson.vimeoId.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.PlayCircle,
                        contentDescription = stringResource(R.string.icon_ver_leccion),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
    }
}

// Función que muestra un módulo
@Composable
fun ModuleCard(
    module: Module, // Datos del módulo
    onClick: () -> Unit,
    cardColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        ListItem(
            headlineContent = {
                Text( // Título del módulo
                    text = module.name,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = cardColor
            ),
            supportingContent = { // Muestra el número de lecciones del módulo
                Text(
                    text = stringResource(R.string.lecciones, module.lessonCount),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            // Muestra un icono de escuela en la parte izquierda
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}
