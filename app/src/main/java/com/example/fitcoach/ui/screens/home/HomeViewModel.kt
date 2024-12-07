package com.example.fitcoach.ui.screens.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fitcoach.R
import com.example.fitcoach.ui.screens.home.model.BlogPost
import com.example.fitcoach.ui.screens.home.model.Category
import com.example.fitcoach.ui.screens.home.model.ExerciseCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// ViewModel para la pantalla de inicio
class HomeViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    // Referencia a la base de datos Firestore
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var sharedPreferences: SharedPreferences

    // Inicializa el archivo de preferencias compartidas
    fun initSharedPreferences(context: Context) {
        // Abre el mismo archivo "login_prefs" que tiene LoginViewModel
        sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
    }

    var userName by mutableStateOf("Usuario")
        private set

    var exercises by mutableStateOf<List<ExerciseCategory>>(emptyList())
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    var blogPost by mutableStateOf(BlogPost(title = "Blog", imageResource = R.drawable.blog_img))
        private set

    var showContactDialog by mutableStateOf(false)
        private set

    var hasNotifications by mutableStateOf(false)
        private set

    var showUnderDevelopmentDialog by mutableStateOf(false)
        private set


    init {
        loadInitialData() // Carga los datos iniciales
        loadUserName() // Carga el nombre del usuario
    }

    // Carga los datos iniciales
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
        ).sortedBy { it.name }

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

    fun onExerciseClick(exercise: ExerciseCategory, onNavigate: (String) -> Unit) {
        onNavigate("exercises?muscleGroup=${exercise.name}")
    }

    fun onCategoryClick(category: Category, onNavigate: (String) -> Unit) {
        when (category.name) {
            "Entrenamiento" -> onNavigate("training")
            "Academia" -> onNavigate("academy")
            "Progreso" -> onNavigate("progress")
            "Tienda" -> onNavigate("store")
        }
    }

    // Abre el enlace a la red social correspondiente
    fun onSocialClick(network: String, context: Context) {
        val url = when (network) {
            "instagram" -> "https://www.instagram.com/sergiomcoach/?hl=es"
            "youtube" -> "https://www.youtube.com/@SergioMCoach"
            "spotify" -> "https://open.spotify.com/playlist/4sYAoF4S6gyrq91h77wbGT?si=Q0XIZI02SfG624He7xGyvw&nd=1&dlsi=c6e891c7bfe84b83"
            else -> return
        }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No se pudo abrir el enlace", Toast.LENGTH_SHORT).show()
        }
    }

    // Muestra el diálogo de contacto
    fun onShowContactDialog() {
        showContactDialog = true
    }

    // Oculta el diálogo de contacto
    fun onDismissContactDialog() {
        showContactDialog = false
    }

    fun showDevelopmentDialog() {
        showUnderDevelopmentDialog = true
    }

    fun dismissDevelopmentDialog() {
        showUnderDevelopmentDialog = false
    }

    // Carga el nombre del usuario
    // Busca el nombre del usuario en la colección "users" de Firestore
    // usando el ID del usuario actual
    private fun loadUserName() {
        auth.currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userName = document.getString("name") ?: "Usuario"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("HomeViewModel", "Error al cargar el nombre del usuario", exception)
                }
        }
    }

    // Maneja el cierre de sesión
    fun onLogout(onNavigateToLogin: () -> Unit) {
        try {
            sharedPreferences.edit().clear().apply()
            auth.signOut()
            onNavigateToLogin()
        } catch (e: Exception) {
            Log.e("Logout", "Error durante el logout: ${e.message}")
            onNavigateToLogin()
        }

    }
}