package com.example.fitcoach.ui.screens.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fitcoach.R
import com.google.firebase.auth.FirebaseAuth

// Clase que contiene la lógica de la pantalla de inicio de sesión
class LoginViewModel : ViewModel() {
    // Instancia de FirebaseAuth para autenticación
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Instancia de SharedPreferences para guardar el estado de rememberMe
    private lateinit var sharedPreferences: SharedPreferences


    // Variables que almacenan el estado de los campos de texto y otros elementos de la pantalla
    // Son privadas y solo se pueden modificar desde esta clase
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

    var isLoading by mutableStateOf(false)
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    // Propiedad que indica si hay errores en los campos de texto
    val hasErrors: Boolean
        get() = emailError != null || passwordError != null || errorMessage.isNotEmpty()

    // Actualiza el valor de email y elimina el error
    fun onEmailChange(email: String) {
        this.email = email
        emailError = null
        errorMessage = ""
    }

    // Actualiza el valor de password y elimina el error
    fun onPasswordChange(password: String) {
        this.password = password
        passwordError = null
        errorMessage = ""
    }

    // Función que se llama cuando se cambia la visibilidad de la contraseña
    fun onPasswordVisibilityChange() {
        isPasswordVisible = !isPasswordVisible
    }

    // Función que se llama cuando se cambia el estado de rememberMe
    fun onRememberMeChange(checked: Boolean) {
        rememberMe = checked
    }

    // Muestra el diálogo de contacto
    fun onShowContactDialog() {
        showContactDialog = true
    }

    // Oculta el diálogo de contacto
    fun onDismissContactDialog() {
        showContactDialog = false
    }

    // Función que se llama cuando se hace clic en "Recuperar contraseña"
    fun onPasswordRecoveryClick() {
        showPasswordRecoveryDialog = true
    }

    fun onPasswordRecoveryDialogDismiss() {
        showPasswordRecoveryDialog = false
    }


    // Inicializa el SharedPreferences y carga el estado de rememberMe
    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        // Cargar el estado de rememberMe por defecto es true
        rememberMe = sharedPreferences.getBoolean("remember_me", true)
    }

    // Función que valida los campos de texto
    private fun validateFields(context: Context): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            emailError = context.getString(R.string.error_email)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = context.getString(R.string.error_mail_valido)
            isValid = false
        }

        if (password.isEmpty()) {
            passwordError = context.getString(R.string.error_pass)
            isValid = false
        } else if (password.length < 6) {
            passwordError = context.getString(R.string.error_pass_length)
            isValid = false
        }

        return isValid
    }

    fun login(context: Context, onNavigateToHome: () -> Unit) {
        if (!validateFields(context)) return

        isLoading = true
        errorMessage = ""

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false

                if (task.isSuccessful) {
                    if (rememberMe) {
                        sharedPreferences.edit()
                            .putBoolean("remember_me", true)
                            .apply()
                    }
                    onNavigateToHome()
                } else {
                    errorMessage =
                        context.getString(R.string.error_login_generic, task.exception?.message)
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


    fun recoverPassword(context: Context, email: String, onSuccess: () -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage = context.getString(
                        R.string.error_recuperacion_password,
                        task.exception?.message
                    )
                }
            }
    }


}