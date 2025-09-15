package com.meyrforge.tomabien.my_medications.domain

import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.data.entities.MedicationEntity
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.domain.models.MedicationWithAlarmsDomain

interface MedicationRepository {
    suspend fun saveMedication(med: Medication): Long?
    suspend fun getAllMedications(): List<Medication>?
    suspend fun editMedication(med: Medication): Long?
    suspend fun deleteMedication(med: Medication): Long?
    suspend fun deleteAllMedications()
    suspend fun addAlarm(alarm: Alarm): Long?
    suspend fun deleteAlarm(alarm: Alarm): Int
    suspend fun getAlarms(medicationId: Int): MedicationWithAlarmsDomain
    suspend fun getMedicationById(medicationId: Int): Medication?
}