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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitcoach.ui.screens.home.components.CommonBottomBar
import com.example.fitcoach.ui.theme.BackgroundDark
import com.example.fitcoach.ui.theme.BackgroundLight
import com.example.fitcoach.ui.theme.CardDark
import com.example.fitcoach.ui.theme.CardLight
import com.example.fitcoach.utils.VideoPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademyScreen(
    navController: NavHostController,
    viewModel: AcademyViewModel,
    initialModuleId: String? = null
) {
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }

    LaunchedEffect(initialModuleId) {
        viewModel.initialize(initialModuleId)
    }

    val modules by viewModel.modules.collectAsState()
    val lessons by viewModel.lessons.collectAsState()
    val selectedModuleId by viewModel.selectedModuleId.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedModuleId?.let {
                            modules.find { it.id == selectedModuleId }?.name
                        } ?: "Academia",
                        fontSize = if (isPortrait) 18.sp else 20.sp
                    )
                },

                navigationIcon = {
                    if (selectedModuleId != null) {
                        IconButton(onClick = { viewModel.clearSelectedModule() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
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
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                error != null -> {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

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
                            ModuleCard(
                                module = module,
                                onClick = { viewModel.selectModule(module.id) },
                                cardColor = cardColor
                            )
                        }
                    }
                }

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

@Composable
fun LessonItem(
    lesson: Lesson,
    cardColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        ListItem(
            headlineContent = {
                Text(text = lesson.name)
            },
            trailingContent = {
                if (lesson.vimeoId.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.PlayCircle,
                        contentDescription = "Ver lección",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun ModuleCard(
    module: Module,
    onClick: () -> Unit,
    cardColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = module.name,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = {
                Text(
                    text = "${module.lessonCount} lecciones",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
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
