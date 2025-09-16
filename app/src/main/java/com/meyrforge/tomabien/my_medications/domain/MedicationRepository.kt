package com.meyrforge.tomabien.my_medications.domain

import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.domain.models.MedicationWithAlarmsDomain
import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.common.RepositoryError

interface MedicationRepository {
    suspend fun saveMedication(med: Medication): Result<Long, RepositoryError>
    suspend fun getAllMedications(): Result<List<Medication>, RepositoryError>
    suspend fun editMedication(med: Medication): Result<Long, RepositoryError>
    suspend fun deleteMedication(med: Medication): Result<Long, RepositoryError>
    suspend fun deleteAllMedications(): Result<Unit, RepositoryError>
    suspend fun addAlarm(alarm: Alarm): Result<Long, RepositoryError>
    suspend fun deleteAlarm(alarm: Alarm): Result<Int, RepositoryError>
    suspend fun getAlarms(medicationId: Int): Result<MedicationWithAlarmsDomain, RepositoryError>
    suspend fun getMedicationById(medicationId: Int): Result<Medication?, RepositoryError>

}