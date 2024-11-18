package com.example.fitcoach.ui.screens.home


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fitcoach.ui.navigation.Screen
import com.example.fitcoach.ui.screens.home.components.CommonBottomBar
import com.example.fitcoach.ui.screens.home.components.DrawerContent
import com.example.fitcoach.ui.screens.home.components.ExerciseLibrary
import com.example.fitcoach.ui.screens.home.components.Greeting
import com.example.fitcoach.ui.screens.home.components.LatestNews
import com.example.fitcoach.ui.screens.home.components.OtherCategories
import com.example.fitcoach.ui.screens.home.components.TopAppBarHome
import com.example.fitcoach.ui.theme.BackgroundDark
import com.example.fitcoach.ui.theme.BackgroundLight
import com.example.fitcoach.ui.theme.CardDark
import com.example.fitcoach.ui.theme.CardLight
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}


@Composable
fun HomeScreen(navController: NavHostController, vm: HomeViewModel = viewModel()) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight
    val textColor = if (isDarkTheme) Color.White else Color.Black

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val context = LocalContext.current

    // Inicializar SharedPreferences
    LaunchedEffect(Unit) {
        vm.initSharedPreferences(context)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                userName = vm.userName,
                backgroundColor = backgroundColor,
                drawerState = drawerState,
                onProfileClick = { vm.onProfileClick() },
                onSettingsClick = { /*vm.onSettingsClick()*/ },
                onSocialClick = { network -> vm.onSocialClick(network, context) },
                onSupportClick = { type -> vm.onSupportClick(type) },
                onLogoutClick = {
                    vm.onLogout {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBarHome(
                    isDarkTheme = isDarkTheme,
                    onProfileClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },

            bottomBar = { CommonBottomBar(navController, isDarkTheme) },
            containerColor = backgroundColor
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Greeting(vm.userName, textColor)
                ExerciseLibrary(
                    vm.exercises,
                    textColor,
                    onExerciseClick = { vm.onExerciseClick(it) }) // Se añade el parámetro para saber qué ejercicio se ha seleccionado
                OtherCategories(vm.categories, onCategoryClick = { vm.onCategoryClick(it) })
                LatestNews(
                    vm.blogPost,
                    cardColor,
                    onBlogClick = { vm.onBlogClick() }) // No se añade el parámetro porque solo hay un elemento
            }
        }
    }
}
