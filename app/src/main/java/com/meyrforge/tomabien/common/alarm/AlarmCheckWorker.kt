package com.meyrforge.tomabien.common.alarm

import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.meyrforge.tomabien.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.core.content.edit

class AlarmCheckWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    companion object {
        private const val PREFS_NAME = "alarm_prefs"
        private const val WINDOW_AFTER_MS = 30 * 60 * 1000L // 30 MINS
    }

    private val prefs: SharedPreferences = applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val requestCode = inputData.getInt("requestCode", -1)
            val hour = inputData.getInt("hour", -1)
            val minute = inputData.getInt("minute", -1)
            val medName = inputData.getString("medName") ?: "tu medicina"

            if (requestCode == -1 || hour == -1 || minute == -1) {
                return@withContext Result.failure()
            }

            val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time)
            val key = "alarm_${requestCode}_last_shown"
            val lastShown = prefs.getString(key, "")

            if (lastShown == today) {
                return@withContext Result.success()
            }

            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val now = System.currentTimeMillis()
            val scheduledTime = cal.timeInMillis

            if (now >= scheduledTime && now <= scheduledTime + WINDOW_AFTER_MS){
                showNotification(requestCode, "¡Hora de tu medicina!"," Es momento de tomar $medName.")
                prefs.edit { putString(key, today) }
            }

            return@withContext Result.success()
        } catch (e: Exception) {
            return@withContext Result.retry()
        }
    }

    private fun showNotification(id: Int, title: String, text: String) {
        val builder = NotificationCompat.Builder(applicationContext, "TomaBien")
            .setSmallIcon(R.drawable.new_tb_small_icon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val nm = NotificationManagerCompat.from(applicationContext)
        try {
            nm.notify(id + 10000, builder.build())
        } catch (_: SecurityException){

        }
    }
}