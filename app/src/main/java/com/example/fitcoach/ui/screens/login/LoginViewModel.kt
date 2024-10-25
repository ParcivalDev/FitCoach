package com.example.fitcoach.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel:ViewModel() {
    var email by  mutableStateOf("")
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


    fun onEmailChange(email: String) {
        this.email = email
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun onPasswordVisibilityChange() {
        isPasswordVisible = !isPasswordVisible
    }

    fun onRememberMeChange(checked: Boolean) {
        rememberMe = checked
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




}