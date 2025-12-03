package com.meyrforge.tomabien.common.alarm

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.workDataOf
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.meyrforge.tomabien.my_medications.domain.AlarmScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AndroidAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : AlarmScheduler {

    @Inject
    lateinit var alarmManager: AlarmManager

    override fun schedule(hour: Int, minute: Int, requestCode: Int, medName: String): String {

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("message", "¡Hora de tu medicina! Es momento de tomar $medName.")
                putExtra("alarm_id", requestCode)
            }

            val pendingIntentFlags =
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            val pendingIntent =
                PendingIntent.getBroadcast(context, requestCode, intent, pendingIntentFlags)

            try {
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )

                val now = System.currentTimeMillis()
                var initialDelayMillis = calendar.timeInMillis - now
                if (initialDelayMillis < 0) initialDelayMillis = 0

                val input = workDataOf(
                    "requestCode" to requestCode,
                    "hour" to hour,
                    "minute" to minute,
                    "medName" to medName
                )

                val periodicWork = PeriodicWorkRequestBuilder<AlarmCheckWorker>(
                    24, TimeUnit.HOURS,
                    15, TimeUnit.MINUTES
                )
                    .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
                    .setInputData(input)
                    .build()

                val uniqueName = "alarm_backup_$requestCode"
                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    uniqueName,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    periodicWork
                )


                val hourFormatted = if (hour.toString().length == 1) {
                    "0$hour"
                } else hour.toString()
                val minuteFormatted = if (minute.toString().length == 1) {
                    "0$minute"
                } else minute.toString()
                return "Alarma programada para las $hourFormatted:$minuteFormatted, todos los días."
            } catch (e: SecurityException) {
                return "No se pudo programar la alarma por restricciones del sistema"
            }
        } else {
            return "El permiso para mostrar notificaciones no fue otorgado"
        }
    }

    override fun cancel(requestCode: Int, medName: String): String {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("message", "¡Hora de tu medicina! Es momento de tomar $medName.")
            putExtra("alarm_id", requestCode)
        }
        val pendingIntentFlags =
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent =
            PendingIntent.getBroadcast(context, requestCode, intent, pendingIntentFlags)

        alarmManager.cancel(pendingIntent)

        val uniqueName = "alarm_backup_$requestCode"
        WorkManager.getInstance(context).cancelUniqueWork(uniqueName)

        return "La alarma fue cancelada"
    }
}