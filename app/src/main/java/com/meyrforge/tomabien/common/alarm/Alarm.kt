package com.meyrforge.tomabien.common.alarm

data class Alarm(
    val requestCode: Int,
    val hour: Int,
    val minute: Int,
    val ownerId: Int,
    val dosage: Float = 1.0f
)
