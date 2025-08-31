package com.meyrforge.tomabien.my_medications.domain.models

import com.meyrforge.tomabien.common.alarm.Alarm

data class MedicationWithAlarmsDomain(val medId: Int, val alarms: List<Alarm>)
