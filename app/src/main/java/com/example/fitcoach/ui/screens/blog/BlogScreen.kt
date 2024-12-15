package com.example.fitcoach.ui.screens.blog

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fitcoach.R
import com.example.fitcoach.ui.screens.home.components.CommonBottomBar
import com.example.fitcoach.ui.theme.AccentOrange
import com.example.fitcoach.ui.theme.BackgroundDark
import com.example.fitcoach.ui.theme.BackgroundLight
import com.example.fitcoach.ui.theme.CardDark
import com.example.fitcoach.ui.theme.CardLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(
    navController: NavHostController,
    viewModel: BlogViewModel = viewModel()
) {
    // Colores para el tema claro y oscuro
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight

    val context = LocalContext.current // Contexto para abrir el navegador
    val articles by viewModel.articles.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Scaffold( // Barra superior y barra inferior
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.blog)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.icon_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = cardColor)
            )
        },
        bottomBar = { CommonBottomBar(navController, isDarkTheme, isPortrait = isPortrait) },
        containerColor = backgroundColor
    ) { padding ->
        Column( // Columna con las categorías y los artículos
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Categorías en una fila con scroll
            LazyRow(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val categories = listOf(
                    context.getString(R.string.todos),
                    context.getString(R.string.hipertrofia),
                    context.getString(R.string.recomp_corporal),
                    context.getString(R.string.lesiones),
                    context.getString(R.string.salud),
                    context.getString(R.string.suplementos)
                )
                // Crea un chip por cada categoría
                items(categories) { category ->
                    CategoryChip(
                        category = category,
                        // Si la categoría es "Todos" se selecciona si selectedCategory es null
                        // Si no, se selecciona si selectedCategory es igual a la categoría
                        selected = if (category == "Todos") selectedCategory == null else selectedCategory == category,
                        onClick = {
                            // Si es "Todos" filtra por todas las categorías
                            // Si no, filtra por la categoría seleccionada
                            viewModel.selectCategory(if (category == "Todos") null else category)
                        }
                    )
                }
            }

            // Muestra un indicador de carga si isLoading es true
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Filtra los artículos por categoría seleccionada
                // Si no hay categoría seleccionada, muestra todos los artículos
                // articles.filter { true } devuelve todos los artículos
                val filteredArticles = articles.filter {
                    selectedCategory == null || it.categories.contains(selectedCategory)
                }

                // Muestra los artículos en una lista con scroll
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredArticles) { article ->
                        // Crea una card por cada artículo
                        BlogArticleCard(
                            article = article,
                            cardColor = cardColor,
                            // Abre el navegador al hacer clic en la card
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

// Chip para las categorías
@Composable
fun CategoryChip(
    category: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    FilterChip(
        selected = selected,
        onClick = onClick,
        enabled = true,
        label = {
            Text(
                text = category, // Nombre de la categoría
                style = MaterialTheme.typography.bodyMedium
            )
        },
        shape = RoundedCornerShape(16.dp),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            // Color del borde del chip casi transparente
            borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            // Color del borde del chip seleccionado transparente
            selectedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0f)
        ),
        colors = FilterChipDefaults.filterChipColors(
            labelColor = if (isDarkTheme) Color.Gray // Gris en tema oscuro
            else Color.DarkGray, // Gris oscuro en tema claro
            selectedLabelColor = Color.White,
            selectedContainerColor = if (isDarkTheme) AccentOrange // Color de fondo cuando está seleccionado
            else AccentOrange.copy(alpha = 0.9f),
            containerColor = Color.LightGray.copy(alpha = 0.2f)
        )
    )
}

@Composable
fun BlogArticleCard(
    article: BlogArticle, // Artículo a mostrar
    cardColor: Color,
    onClick: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    // Color de fondo y texto para las categorías del artículo
    val categoryColor = if (isDarkTheme) {
        AccentOrange.copy(alpha = 0.1f)
    } else {
        Color(0xFFE65100).copy(alpha = 0.1f) // Fondo del tag con el naranja más oscuro
    }

    val categoryTextColor = if (isDarkTheme) {
        AccentOrange
    } else {
        Color(0xFFE65100) // Texto del tag con el naranja más oscuro
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Hace clic en la card
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Fila con las categorías y la fecha del artículo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Muestra un tag por cada categoría del artículo en una fila con scroll
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(article.categories) { category ->
                        Surface(
                            color = categoryColor,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = category,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = categoryTextColor
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = article.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            // Título del artículo
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            // Texto del artículo con un máximo de 3 líneas y puntos suspensivos
            Text(
                text = article.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Texto "Leer más" con una flecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.leer_mas),
                    style = MaterialTheme.typography.labelLarge,
                    color = categoryTextColor,
                    modifier = Modifier.padding(4.dp)
                )
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.icon_back),
                    tint = categoryTextColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}


