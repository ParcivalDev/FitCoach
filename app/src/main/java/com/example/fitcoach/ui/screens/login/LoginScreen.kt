package com.example.fitcoach.ui.screens.login

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitcoach.R
import com.example.fitcoach.ui.screens.login.components.EmailField
import com.example.fitcoach.ui.screens.login.components.IconHelp
import com.example.fitcoach.ui.screens.login.components.LoginBackground
import com.example.fitcoach.ui.screens.login.components.LoginButton
import com.example.fitcoach.ui.screens.login.components.LoginOptions
import com.example.fitcoach.ui.screens.login.components.PasswordField
import com.example.fitcoach.utils.ContactDialog
import com.example.fitcoach.utils.DialogUtils
import com.example.fitcoach.ui.screens.login.dialog.PasswordRecoveryDialog


@Composable
@Preview
fun PreviewLoginScreen() {
    LoginScreen(onNavigateToHome = {}, vm = LoginViewModel())
}

// Pantalla de inicio de sesión
@Composable
fun LoginScreen(onNavigateToHome: () -> Unit, vm: LoginViewModel = viewModel()) {

    val context = LocalContext.current
    //Herramienta que permite controlar el enfoque en la interfaz de usuario
    //Se utiliza para quitar el foco de los campos de texto al hacer clic en otro lugar
    val focusManager = LocalFocusManager.current
    //Herramienta que permite controlar el teclado en la interfaz de usuario
    val keyboardController = LocalSoftwareKeyboardController.current

    // Obtener la configuración actual del dispositivo
    val configuration = LocalConfiguration.current
    // Determinar si el dispositivo está en modo vertical
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT


    // Inicializar SharedPreferences
    LaunchedEffect(Unit) {
        vm.initSharedPreferences(context)
        // Verificar si hay una sesión guardada y navegar a la pantalla de home si es así
        vm.checkSavedSession(onNavigateToHome)
    }

    // Componente principal de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                // Herramienta que permite detectar interacciones en la interfaz de usuario
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                //Elimina el foco de los campos de texto y oculta el teclado al hacer clic
                //en cualquier lugar de la pantalla
                focusManager.clearFocus()
                keyboardController?.hide()
            },
        contentAlignment = Alignment.Center
    ) {
        // Coloca el fondo de la pantalla
        LoginBackground(isPortrait)

        // Coloca el icono de ayuda en la esquina superior derecha
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            IconHelp(
                onShowContactDialog = { vm.onShowContactDialog() },
                isPortrait = isPortrait
            )
        }

        // Coloca el contenido principal de la pantalla
        LoginCard(vm = vm, onNavigateToHome = onNavigateToHome, isPortrait, context = context)
    }

    // Muestra el diálogo de contacto si es necesario
    if (vm.showContactDialog) {
        ContactDialog(onDismiss = { vm.onDismissContactDialog() },
            onWhatsAppClick = { DialogUtils.handleWhatsApp(context) },
            onEmailClick = { DialogUtils.handleEmail(context) })
    }

    // Muestra el diálogo de recuperación de contraseña si es necesario
    if (vm.showPasswordRecoveryDialog) {
        PasswordRecoveryDialog(
            onDismiss = { vm.onPasswordRecoveryDialogDismiss() },
            onRecover = { email ->
                vm.recoverPassword(
                    context = context,
                    email = email,
                    onSuccess = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.password_recovery_sent),
                            Toast.LENGTH_LONG
                        ).show()
                        vm.onPasswordRecoveryDialogDismiss()
                    }
                )
            }
        )
    }
}

// Componente que contiene los campos de texto y botones de inicio de sesión
@Composable
private fun LoginCard(
    vm: LoginViewModel,
    onNavigateToHome: () -> Unit,
    isPortrait: Boolean,
    context: Context
) {
    // Tarjeta con fondo blanco que contiene el formulario de inicio de sesión
    Card(
        modifier = Modifier
            .fillMaxWidth(if (isPortrait) 0.9f else 0.8f)
            .padding(
                horizontal = if (isPortrait) 16.dp else 32.dp,
                vertical = if (isPortrait) 0.dp else 16.dp
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        // Columna con scroll que contiene los campos de texto y botones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = if (isPortrait) 16.dp else 24.dp,
                    vertical = if (isPortrait) 16.dp else 24.dp
                )
        ) {
            EmailField( // Campo de texto para el email
                email = vm.email, // Valor del email desde el ViewModel
                onEmailChange = { vm.onEmailChange(it) }, // Función que se llama cuando cambia el email
                isError = vm.emailError != null, // Indica si hay un error en el campo
                errorMessage = vm.emailError ?: "", // Mensaje de error o cadena vacía
                isPortrait = isPortrait // Indica si el dispositivo está en modo vertical
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField( // Campo de texto para la contraseña
                password = vm.password,
                isPasswordVisible = vm.isPasswordVisible,
                onPasswordChange = { vm.onPasswordChange(it) },
                onPasswordVisibilityChange = { vm.onPasswordVisibilityChange() },
                isError = vm.passwordError != null,
                errorMessage = vm.passwordError ?: "",
                isPortrait = isPortrait
            )

            Spacer(modifier = Modifier.height(12.dp))

            LoginOptions( // Opciones de inicio de sesión
                rememberMe = vm.rememberMe,
                onRememberMeChange = { vm.onRememberMeChange(it) },
                onPasswordRecoveryClick = { vm.onPasswordRecoveryClick() },
                isPortrait = isPortrait
            )

            // Mensaje de error de Firebase
            if (vm.errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = vm.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = if (isPortrait) 14.sp else 12.sp
                    ),
                    modifier = Modifier.padding(
                        horizontal = if (isPortrait) 4.dp else 2.dp
                    )
                )
            }



            Spacer(modifier = Modifier.height(24.dp))

            // Botón de inicio de sesión
            LoginButton(
                onNavigateToHome = { vm.login(context) { onNavigateToHome() } },
                isLoading = vm.isLoading,
                enabled = !vm.hasErrors,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                isPortrait = isPortrait
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}




