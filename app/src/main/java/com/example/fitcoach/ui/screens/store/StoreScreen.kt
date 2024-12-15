package com.example.fitcoach.ui.screens.store

import android.annotation.SuppressLint
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
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
fun StoreScreen(
    navController: NavHostController,
    viewModel: StoreViewModel = viewModel()
) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight

    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    // Estado para el filtro
    var filter by remember { mutableStateOf("Todos") }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.tienda)) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Barra de filtro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { filter = "Todos" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (filter == "Todos") AccentOrange else cardColor
                    )
                ) {
                    Text(text = "Todos")
                }

                Button(
                    onClick = { filter = "Gratis" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (filter == "Gratis") AccentOrange else cardColor
                    )
                ) {
                    Text(text = "Gratis")
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Filtrar productos según el estado del filtro
                val filteredProducts = when (filter) {
                    "Gratis" -> products.filter { it.isFree || it.price == 0.0 }
                    else -> products
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            cardColor = cardColor,
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(product.url))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

// Muestra la card de un producto
@SuppressLint("DefaultLocale")
@Composable
fun ProductCard(
    product: Product,
    cardColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Imagen del producto según el título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Image(
                    painter = painterResource(
                        when (product.title) {
                            "Guía de ejercicios Físico + [REGALO]" -> R.drawable.guia_ejercicio
                            "E-Book Push The Limits" -> R.drawable.push_the_limits
                            "E-book Push The Limits (Mujer)" -> R.drawable.push_mujer
                            "E-book Building Deltoids" -> R.drawable.building_deltoids
                            "CoronaEbook 2.0" -> R.drawable.coronaebook
                            "Manual de Ejercicio LITE" -> R.drawable.manual_lite
                            "Diario de Etto. PDF" -> R.drawable.diario_etto
                            else -> R.drawable.ic_image_not_found // Imagen por defecto
                        }
                    ),
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f) // Cuadrada
                        .alpha(0.4f), // Transparencia
                    contentScale = ContentScale.Crop
                )
            }

            // Información del producto (título, descripción y precio)
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Precio del producto (si es gratis, se muestra "Gratis")
                // Surface para redondear los bordes del texto del precio y darle un color de fondo
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (product.isFree || product.price == 0.0) {
                            stringResource(R.string.gratis)
                        } else {
                            String.format(
                                "%.2f€",
                                product.price
                            ) // Formatear a 2 decimales y añadir el símbolo del euro
                        },
                        modifier = Modifier.padding(12.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

            }
        }
    }

}

