package com.example.fitcoach.ui.screens.exercises

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fitcoach.ui.screens.home.HomeViewModel
import com.example.fitcoach.ui.screens.home.components.CommonBottomBar
import com.example.fitcoach.ui.theme.BackgroundDark
import com.example.fitcoach.ui.theme.BackgroundLight
import com.example.fitcoach.ui.theme.CardDark
import com.example.fitcoach.ui.theme.CardLight
import kotlinx.coroutines.launch


private fun getVideoUrl(exercise: Exercise): String {
    return if (exercise.vimeoHash.isNotEmpty()) {
        "https://player.vimeo.com/video/${exercise.vimeoId}?h=${exercise.vimeoHash}&badge=0&autopause=0&player_id=0&app_id=58479"
    } else {
        "https://player.vimeo.com/video/${exercise.vimeoId}?badge=0&autopause=0&player_id=0&app_id=58479"
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLibraryScreen(
    navController: NavHostController,
    viewModel: ExerciseLibraryViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    initialMuscle: String? = null
) {
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredExercises by viewModel.filteredExercises.collectAsState()
    LaunchedEffect(initialMuscle) {
        viewModel.initialize(initialMuscle)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight

    val exercises by viewModel.exercises.collectAsState()
    val selectedMuscleGroup by viewModel.selectedMuscleGroup.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = backgroundColor) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Grupos Musculares",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(20.dp)
                )
                HorizontalDivider()

                LazyColumn {  // Reemplazamos el forEach por una LazyColumn
                    items(homeViewModel.exercises) { exercise ->
                        NavigationDrawerItem(
                            label = { Text(exercise.name) },
                            selected = selectedMuscleGroup == exercise.name,
                            onClick = {
                                viewModel.selectMuscleGroup(exercise.name)
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(selectedMuscleGroup ?: "Biblioteca de Ejercicios") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, "Abrir menú")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = cardColor)
                    )
                },
                bottomBar = { CommonBottomBar(navController, isDarkTheme, isPortrait = true) },
                containerColor = backgroundColor
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    when {
                        isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        error != null -> Text(
                            text = "Error: ${error}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )

                        exercises.isEmpty() && selectedMuscleGroup != null -> Text(
                            text = "No hay ejercicios disponibles para este grupo muscular",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )

                        else ->
                            Column {
                                SearchBar(
                                    query = searchQuery,
                                    onQueryChange = { viewModel.onSearchQueryChange(it) }
                                )
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    contentPadding = PaddingValues(bottom = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(filteredExercises) { exercise ->
                                        ListItem(
                                            headlineContent = {
                                                Text(
                                                    text = exercise.name,
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
                                                if (exercise.vimeoId.isNotEmpty()) {
                                                    Icon(
                                                        Icons.Default.PlayCircle,
                                                        contentDescription = "Ver ejercicio",
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

            if (selectedExercise != null) {
                Dialog(
                    onDismissRequest = { selectedExercise = null },
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,  // Esto asegura que se pueda cerrar al tocar fuera
                        usePlatformDefaultWidth = false,
                        decorFitsSystemWindows = false  // Esto asegura que el dialog se ajuste correctamente
                    )
                ) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(onClick = { selectedExercise = null }),  // Añadimos clickable aquí también
                        contentAlignment = Alignment.Center
                    ) {
                        val width = if (maxWidth > maxHeight) {
                            // En modo landscape, limitamos el ancho basado en la altura
                            (maxHeight * (16f / 9f)).coerceAtMost(maxWidth * 0.8f)
                        } else {
                            // En modo portrait, usamos casi todo el ancho disponible
                            maxWidth * 0.9f
                        }

                        Card(
                            modifier = Modifier
                                .width(width)
                                .aspectRatio(16f / 9f)
                                .padding(8.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                AndroidView(
                                    factory = { context ->
                                        WebView(context).apply {
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                            )
                                            webViewClient = WebViewClient()
                                            settings.apply {
                                                //javaScriptEnabled = true
                                                loadWithOverviewMode = true
                                                useWideViewPort = true
                                                domStorageEnabled = true
                                                allowContentAccess = true
                                                allowFileAccess = true
                                                mediaPlaybackRequiresUserGesture = false
                                                setSupportMultipleWindows(true)
                                            }

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
                                                    src="${getVideoUrl(selectedExercise!!)}"
                                                    frameborder="0" 
                                                    allow="autoplay; fullscreen; picture-in-picture"
                                                    allowfullscreen 
                                                    style="position:absolute;top:0;left:0;width:100%;height:100%;">
                                                </iframe>
                                            </body>
                                        </html>
                                        """.trimIndent()

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
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Buscar ejercicio...") },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Buscar"
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