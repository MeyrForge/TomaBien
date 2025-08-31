package com.meyrforge.tomabien.common

sealed class Screen(val route: String) {
    data object MyMedications : Screen("my_medications")
    data object Alarms: Screen("alarms")
}