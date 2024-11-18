package com.example.fitcoach.ui.screens.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.fitcoach.R
import com.example.fitcoach.MainActivity

// Servicio de notificaciones
object NotificationService {
    // Identificador del canal de notificación
    private const val CHANNEL_ID_TIMER = "timer_channel"
    private const val NOTIFICATION_ID_TIMER = 1

    // Crea un canal de notificación para el temporizador
    fun createNotificationChannel(context: Context) {
        val nombre = context.getString(R.string.not_timer_name)
        val descripcion = context.getString(R.string.not_timer_descr)
        // IMPORTANCE_HIGH muestra la notificación en la parte superior de la pantalla
        // y permite que suene
        val nivel = NotificationManager.IMPORTANCE_HIGH
        val canal = NotificationChannel(CHANNEL_ID_TIMER, nombre, nivel).apply {
            description = descripcion
            enableVibration(true) // Habilita la vibración
            vibrationPattern = longArrayOf(0, 300, 200, 300) // Patrón de vibración
        }

        // Obtiene el servicio de notificaciones del sistema y crea el canal
        val notificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(canal)
    }

    // Muestra una notificación cuando el temporizador se completa
    fun timerNotification(context: Context) {
        // Intent para abrir la aplicación al hacer clic en la notificación
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigateToTimer", true) // Parámetro para navegar al temporizador
        }

        // PendingIntent para abrir la aplicación al hacer clic en la notificación
        val flags =
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            flags
        )

        // Construye la notificación
        val builder = NotificationCompat.Builder(context, CHANNEL_ID_TIMER)
            .setSmallIcon(R.drawable.logo_app) // Icono pequeño
            .setContentTitle(context.getString(R.string.descanso_terminado))
            .setContentText(context.getString(R.string.mensaje_not_descanso))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Cierra la notificación al hacer clic en ella
            .setContentIntent(pendingIntent) // Abre la aplicación al hacer clic en la notificación
            .setVibrate(longArrayOf(0, 300, 200, 300))

        // Muestra la notificación
        val notificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID_TIMER, builder.build())
    }
}