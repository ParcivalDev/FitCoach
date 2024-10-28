package com.example.fitcoach.ui.screens.login

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var sharedPreferences: SharedPreferences

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var rememberMe by mutableStateOf(true)
        private set

    var showContactDialog by mutableStateOf(false)
        private set

    var showPasswordRecoveryDialog by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set


    fun onEmailChange(email: String) {
        this.email = email
        errorMessage = ""
    }

    fun onPasswordChange(password: String) {
        this.password = password
        errorMessage = ""
    }

    fun onPasswordVisibilityChange() {
        isPasswordVisible = !isPasswordVisible
    }

    fun onRememberMeChange(checked: Boolean) {
        rememberMe = checked
        sharedPreferences.edit().putBoolean("remember_me", checked).apply()

        if (!checked) {
            // Si se desmarca remember me, limpiar las preferencias
            sharedPreferences.edit().clear().apply()
        }

    }

    fun onShowContactDialog() {
        showContactDialog = true
    }

    fun onDismissContactDialog() {
        showContactDialog = false
    }

    fun onPasswordRecoveryClick() {
        showPasswordRecoveryDialog = true
    }

    fun onPasswordRecoveryDialogDismiss() {
        showPasswordRecoveryDialog = false
    }


    // Función para inicializar SharedPreferences, llamada desde el init
    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        // Cargar el estado previo de rememberMe
        rememberMe = sharedPreferences.getBoolean("remember_me", true)
    }

    fun login(onNavigateToHome: () -> Unit) {
        // Validación básica de campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage = "Por favor, rellene todos los campos"
            return
        }

        // Validación básica de formato de email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Por favor, introduce un email válido"
            return
        }

        // Validación de longitud mínima de contraseña
        if (password.length < 6) {
            errorMessage = "La contraseña debe tener al menos 6 caracteres"
            return
        }


        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("Login", "Inicio de sesión exitoso")

                    // Guardar las preferencias si rememberMe está activo
                    if (rememberMe) {
                        sharedPreferences.edit().apply {
                            putBoolean("remember_me", true)
                            putString("user_email", email)
                            // No guardar la contraseña por seguridad
                            apply()
                        }
                    } else {
                        // Si no está activo, limpiar las preferencias
                        sharedPreferences.edit().clear().apply()
                    }

                    onNavigateToHome()
                } else {
                    errorMessage = when (task.exception?.message) {
                        "ERROR_INVALID_EMAIL" -> "Email inválido"
                        "ERROR_WRONG_PASSWORD" -> "Contraseña incorrecta"
                        "ERROR_USER_NOT_FOUND" -> "Usuario no encontrado"
                        "ERROR_USER_DISABLED" -> "Usuario deshabilitado"
                        "ERROR_TOO_MANY_REQUESTS" -> "Demasiados intentos fallidos. Intente más tarde"
                        "ERROR_OPERATION_NOT_ALLOWED" -> "Operación no permitida"
                        else -> "Error de inicio de sesión: ${task.exception?.message}"

                    }
                }

            }

    }

    // Función para verificar si hay una sesión guardada
    fun checkSavedSession(onNavigateToHome: () -> Unit) {
        val currentUser = auth.currentUser
        val shouldRemember = sharedPreferences.getBoolean("remember_me", false)

        if (currentUser != null && shouldRemember) {
            // Hay una sesión activa y remember me está activo
            onNavigateToHome()
        } else if (!shouldRemember) {
            // Si remember me no está activo, cerrar sesión
            auth.signOut()
        }
    }

}