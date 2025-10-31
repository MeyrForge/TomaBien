package com.meyrforge.tomabien.my_medications.domain

import com.meyrforge.tomabien.common.alarm.Alarm

interface AlarmScheduler {
    fun schedule(hour: Int, minute: Int, requestCode: Int, medName: String): String
    fun cancel(requestCode: Int, medName: String): String
}