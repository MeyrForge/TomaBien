package com.meyrforge.tomabien.my_medications.domain.models

import com.meyrforge.tomabien.common.alarm.Alarm

data class MedicationWithAlarmsDomain(val medId: Int = 0, val alarms: List<Alarm> = emptyList(), val medication: Medication? = null)
