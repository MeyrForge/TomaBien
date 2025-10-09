package com.meyrforge.tomabien

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TomaBienApplication: Application() {
    companion object {
        const val LOW_PILLS_CHANNEL_ID = "low_pills_channel"
    }
}