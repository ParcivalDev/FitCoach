package com.example.fitcoach.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.fitcoach.R

object DialogUtils {
    // Función que maneja la apertura de whatsapp con un mensaje predeterminado
    fun handleWhatsApp(context: Context) {
        val phoneNumber = context.getString(R.string.num_entrenador)
        val message = context.getString(R.string.mensaje_ayuda_whatsapp) // Mensaje predeterminado

        val uri = Uri.parse(
            "https://api.whatsapp.com/send?phone=$phoneNumber&text=${
                Uri.encode(message)
            }"
        )
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {//Intenta abrir whatsapp
            context.startActivity(intent)
        } catch (e: Exception) {
            //Si no tiene whatsapp muestra un mensaje de error
            Toast.makeText(context, context.getString(R.string.sin_whatsapp), Toast.LENGTH_SHORT)
                .show()
        }
    }

    //Función para manejar el envío de correos
    fun handleEmail(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(context.getString(R.string.num_entrenador))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.mensaje_ayuda_email))
        }
        try { //Intenta abrir la app de correo
            context.startActivity(emailIntent)
        } catch (e: Exception) {
            // Si no puede abrirla muestra un error por pantalla
            Toast.makeText(
                context,
                context.getString(R.string.error_abrir_correo),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    // Función que maneja la recuperación de la contraseña
    //SIN FUNCIONALIDAD POR AHORA
    fun handlePasswordRecovery(
        context: Context, email: String
    ) {
        // Muestra un mensaje indicando que se ha enviado un mail de recuperación
        Toast.makeText(
            context,
            context.getString(R.string.email_recuperacion, email),
            Toast.LENGTH_LONG
        ).show()
    }
}