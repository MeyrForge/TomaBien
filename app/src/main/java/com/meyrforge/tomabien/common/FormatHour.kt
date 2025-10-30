package com.meyrforge.tomabien.common

fun formatHour(hour:Int, minute:Int): String{
    val hour = if (hour.toString().length == 1) {
        "0$hour"
    } else hour.toString()
    val minute = if (minute.toString().length == 1) {
        "0$minute"
    } else minute.toString()
    return "$hour:$minute"
}