package com.example.fitcoach.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitcoach.ui.screens.login.components.EmailField
import com.example.fitcoach.ui.screens.login.components.IconHelp
import com.example.fitcoach.ui.screens.login.components.LoginBackground
import com.example.fitcoach.ui.screens.login.components.LoginButton
import com.example.fitcoach.ui.screens.login.components.LoginOptions
import com.example.fitcoach.ui.screens.login.components.PasswordField
import com.example.fitcoach.ui.screens.login.dialogs.ContactDialog
import com.example.fitcoach.utils.DialogUtils
import com.example.fitcoach.ui.screens.login.dialogs.PasswordRecoveryDialog


/*@Composable
@Preview
fun PreviewLoginScreen() {
    LoginScreen(onNavigateToHome = {}, vm = LoginViewModel() )
}*/

@Composable
fun LoginScreen(onNavigateToHome: () -> Unit, vm: LoginViewModel = viewModel()) {

    val context = LocalContext.current
    //Herramienta que permite controlar el enfoque en la interfaz de usuario
    //Se utiliza para quitar el foco de los campos de texto al hacer clic en otro lugar
    val focusManager = LocalFocusManager.current
    //Herramienta que permite controlar el teclado en la interfaz de usuario
    val keyboardController = LocalSoftwareKeyboardController.current


    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                //Rastra la interacción del usuario con el componente
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
        LoginBackground()

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            IconHelp(onShowContactDialog = { vm.onShowContactDialog() })
        }


        LoginCard(vm = vm, onNavigateToHome = onNavigateToHome)
    }

    if (vm.showContactDialog) {
        ContactDialog(onDismiss = { vm.onDismissContactDialog() },
            onWhatsAppClick = { DialogUtils.handleWhatsApp(context) },
            onEmailClick = { DialogUtils.handleEmail(context) })
    }

    if (vm.showPasswordRecoveryDialog) {
        PasswordRecoveryDialog(
            onDismiss = { vm.onPasswordRecoveryDialogDismiss() },
            onRecover = { mail ->
                DialogUtils.handlePasswordRecovery(context, mail)
                vm.onPasswordRecoveryDialogDismiss()
            }
        )
    }


}

@Composable
private fun LoginCard(
    vm: LoginViewModel,
    onNavigateToHome: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            EmailField(
                email = vm.email,
                onEmailChange = { vm.onEmailChange(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(
                password = vm.password,
                isPasswordVisible = vm.isPasswordVisible,
                onPasswordChange = { vm.onPasswordChange(it) },
                onPasswordVisibilityChange = { vm.onPasswordVisibilityChange() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LoginOptions(
                rememberMe = vm.rememberMe,
                onRememberMeChange = { vm.onRememberMeChange(it) },
                onPasswordRecoveryClick = { vm.onPasswordRecoveryClick() })



            Spacer(modifier = Modifier.height(24.dp))

            LoginButton(
                onNavigateToHome = onNavigateToHome,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}




