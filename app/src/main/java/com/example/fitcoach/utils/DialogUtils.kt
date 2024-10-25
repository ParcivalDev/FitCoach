package com.example.fitcoach.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object DialogUtils {
    fun handleWhatsApp(context: Context) {
        val phoneNumber = "+34600000000"
        val message = "Hola, necesito ayuda con mi cuenta de FitCoach." // Mensaje predeterminado

        val uri = Uri.parse(
            "https://api.whatsapp.com/send?phone=$phoneNumber&text=${
                Uri.encode(message)
            }"
        )
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
        }
    }


    fun handleEmail(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:entrenador@ejemplo.com")
            putExtra(Intent.EXTRA_SUBJECT, "Ayuda con mi cuenta de FitCoach")
        }
        try {
            context.startActivity(emailIntent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "No se pudo abrir la aplicación de correo",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun handlePasswordRecovery(
        context: Context, email: String
    ) {
        Toast.makeText(
            context,
            "Se ha enviado un email de recuperación a $email",
            Toast.LENGTH_LONG
        ).show()
    }
}