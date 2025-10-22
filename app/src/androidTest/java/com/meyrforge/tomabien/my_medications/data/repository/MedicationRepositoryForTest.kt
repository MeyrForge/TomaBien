package com.meyrforge.tomabien.my_medications.data.repository

import com.meyrforge.tomabien.common.RepositoryError
import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.domain.models.MedicationWithAlarmsDomain

class MedicationRepositoryForTest : MedicationRepository {

    private val medications = mutableListOf<Medication>()
    private val alarms = mutableListOf<Alarm>()

    override suspend fun saveMedication(med: Medication): Result<Long, RepositoryError> {
        medications.add(med)
        return Result.Success(medications.size.toLong())
    }

    override suspend fun getAllMedications(): Result<List<Medication>, RepositoryError> {
        return Result.Success(medications.toList())
    }

    override suspend fun editMedication(med: Medication): Result<Long, RepositoryError> {
        for (medication in medications){
            if (med.id == medication.id){
                medications.remove(medication)
                medications.add(med)
                return Result.Success(1L)
            }
        }
        return Result.Error(RepositoryError.MedicationNotFound)
    }

    override suspend fun deleteMedication(med: Medication): Result<Long, RepositoryError> {
        medications.remove(med)
        return Result.Success(medications.size.toLong())
    }

    override suspend fun deleteAllMedications(): Result<Unit, RepositoryError> {
        medications.clear()
        return Result.Success(Unit)
    }

    override suspend fun addAlarm(alarm: Alarm): Result<Long, RepositoryError> {
        alarms.add(alarm)
        return Result.Success(alarms.size.toLong())
    }

    override suspend fun deleteAlarm(alarm: Alarm): Result<Int, RepositoryError> {
        alarms.remove(alarm)
        return Result.Success(alarms.size)
    }

    override suspend fun getAlarms(medicationId: Int): Result<MedicationWithAlarmsDomain, RepositoryError> {
        val medication = medications.find { it.id == medicationId }
        if (medication == null) {
            return Result.Error(RepositoryError.MedicationNotFound)
        }
        val alarmsForMedication = alarms.filter { it.ownerId == medicationId }
        val medicationWithAlarms = MedicationWithAlarmsDomain(medicationId, alarmsForMedication, medication)
        return Result.Success(medicationWithAlarms)
    }

    override suspend fun getMedicationById(medicationId: Int): Result<Medication?, RepositoryError> {
        return Result.Success(medications.find { it.id == medicationId })
    }

    override suspend fun updateNumberOfPills(
        medicationId: Int,
        numberOfPills: Float,
    ): Result<Int, RepositoryError> {
        for (medication in medications) {
            if (medicationId == medication.id) {
                return Result.Success(1)
            }
            return Result.Error(RepositoryError.MedicationNotFound)
        }
        return Result.Error(RepositoryError.MedicationNotFound)
    }
}