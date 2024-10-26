package com.example.fitcoach.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fitcoach.R
import com.example.fitcoach.ui.screens.home.model.BlogPost
import com.example.fitcoach.ui.screens.home.model.Category
import com.example.fitcoach.ui.screens.home.model.ExerciseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class HomeViewModel : ViewModel() {
    var userName by mutableStateOf("Usuario")
        private set

    var exercises by mutableStateOf<List<ExerciseCategory>>(emptyList())
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    var blogPost by mutableStateOf(BlogPost(title = "Blog", imageResource = R.drawable.blog_img))
        private set

    var hasNotifications by mutableStateOf(false)
        private set

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        exercises = listOf(
            ExerciseCategory("Pectoral", R.drawable.pectoral_img),
            ExerciseCategory("Espalda", R.drawable.espalda_img),
            ExerciseCategory("Trapecio", R.drawable.trapecio_img),
            ExerciseCategory("Deltoides", R.drawable.deltoides_img),
            ExerciseCategory("Tríceps", R.drawable.triceps_img),
            ExerciseCategory("Bíceps", R.drawable.biceps_img),
            ExerciseCategory("Antebrazo", R.drawable.antebrazo_img),
            ExerciseCategory("Abdomen", R.drawable.abdomen_img),
            ExerciseCategory("Glúteo", R.drawable.gluteo_img),
            ExerciseCategory("Cuádriceps", R.drawable.cuadriceps_img),
            ExerciseCategory("Isquios", R.drawable.isquios_img),
            ExerciseCategory("Gemelos", R.drawable.gemelos_img),
            ExerciseCategory("Aductores", R.drawable.aductores_img)
        )

        categories = listOf(
            Category("Entrenamiento", R.drawable.entrenamiento_img),
            Category("Academia", R.drawable.academia_img),
            Category("Progreso", R.drawable.progreso_img),
            Category("Tienda", R.drawable.tienda_img)
        )
    }

    // Funciones para manejar eventos de UI
    fun onProfileClick() {
        // TODO: Navegación al perfil
    }

    fun onNotificationsClick() {
        // TODO: Abrir notificaciones
    }

    fun onBlogClick() {
        // TODO: Implementar navegación al blog
    }

    fun onExerciseClick(exercise: ExerciseCategory) {
        // TODO: Navegar a ejercicios específicos
    }

    fun onCategoryClick(category: Category) {
        // TODO: Navegar a la categoría específica
        // Ejemplo: navegar según el nombre de la categoría
        when (category.name) {
            "Entrenamiento" -> { /* Navegar a pantalla de entrenamiento */
            }

            "Academia" -> { /* Navegar a pantalla de academia */
            }

            "Progreso" -> { /* Navegar a pantalla de progreso */
            }

            "Tienda" -> { /* Navegar a pantalla de tienda */
            }
        }
    }
}