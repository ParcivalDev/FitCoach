package com.example.fitcoach.ui.screens.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.fitcoach.R
import com.example.fitcoach.MainActivity

object NotificationService {
    private const val CHANNEL_ID_TIMER = "timer_channel"
    private const val NOTIFICATION_ID_TIMER = 1

    // Crea un canal de notificación para el temporizador
    fun createNotificationChannel(context: Context) {
        val name = context.getString(R.string.not_timer_name)
        val descriptionText = context.getString(R.string.not_timer_descr)
        // IMPORTANCE_HIGH muestra la notificación en la parte superior de la pantalla
        // y permite que suene y vibre
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID_TIMER, name, importance).apply {
            description = descriptionText
            enableVibration(true) // Habilita la vibración
            vibrationPattern = longArrayOf(0, 300, 200, 300)
        }

        val notificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    // Muestra una notificación cuando el temporizador se completa
    fun showTimerCompleteNotification(context: Context) {
        // Intent para abrir la aplicación al hacer clic en la notificación
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
            .setSmallIcon(R.drawable.logo_app)
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