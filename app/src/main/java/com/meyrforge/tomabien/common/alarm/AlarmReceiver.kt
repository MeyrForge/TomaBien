package com.meyrforge.tomabien.common.alarm

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.meyrforge.tomabien.MainActivity
import com.meyrforge.tomabien.R

class AlarmReceiver: BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, MainActivity::class.java)
        val requestCode = intent?.getIntExtra("alarm_id", -1) ?: 0
        val message = intent?.getStringExtra("message") ?: "Toma de medicacion"
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, requestCode, i, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context!!, "TomaBien")
            .setSmallIcon(R.drawable.ic_notification_foreground)
            .setContentTitle("TomaBien")
            .setContentText(message)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(requestCode+1, builder.build())
    }
}