package com.meyrforge.tomabien.common.alarm

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.meyrforge.tomabien.MainActivity
import com.meyrforge.tomabien.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.core.content.edit
import kotlin.text.substring

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val PREFS_NAME = "alarm_prefs"
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val i = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val requestCode = intent.getIntExtra("alarm_id", -1)
        val message = intent.getStringExtra("message") ?: "Toma de medicacion"

        val pendingIntent = PendingIntent.getActivity(context, requestCode, i, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, "TomaBien")
            .setSmallIcon(R.drawable.new_tb_small_icon)
            .setContentTitle("TomaBien")
            .setContentText(message)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        try {
            notificationManager.notify(requestCode+1, builder.build())
            rescheduleAlarm(context, intent)
        } catch (_: SecurityException){

        }
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time)
        prefs.edit { putString("alarm_${requestCode}_last_shown", today) }
    }

    private fun rescheduleAlarm(context: Context, oldIntent: Intent) {
        val requestCode = oldIntent.getIntExtra("alarm_id", -1)
        val message = oldIntent.getStringExtra("message")

        if (requestCode == -1 || message == null) {
            return
        }

        val hour = requestCode.toString().substring(1, 3)
        val minute = requestCode.toString().substring(3, 5)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, hour.toInt())
            set(Calendar.MINUTE, minute.toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val newIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtras(oldIntent.extras!!)
        }

        val pendingIntentFlags =
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent =
            PendingIntent.getBroadcast(context, requestCode, newIntent, pendingIntentFlags)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

}