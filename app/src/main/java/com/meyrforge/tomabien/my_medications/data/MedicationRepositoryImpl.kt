package com.meyrforge.tomabien.my_medications.data

import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.data.entities.toAlarmEntity
import com.meyrforge.tomabien.my_medications.data.entities.toDomain
import com.meyrforge.tomabien.my_medications.data.entities.toMedicationEntity
import com.meyrforge.tomabien.my_medications.data.entities.toMedication
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.domain.models.MedicationWithAlarmsDomain
import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(private val medicationDao: MedicationDao) :
    MedicationRepository {
    override suspend fun saveMedication(med: Medication): Long? {
        val medicationEntity = med.toMedicationEntity()
        return medicationDao.insertMedication(medicationEntity)
    }

    override suspend fun getAllMedications(): List<Medication>? {
        return medicationDao.getAllMedications()?.map { it.toMedication() }
    }

    override suspend fun editMedication(med: Medication): Long {
        val medicationEntity = med.toMedicationEntity()
        return medicationDao.updateMedication(medicationEntity).toLong()
    }

    override suspend fun deleteMedication(med: Medication): Long {
        val medicationEntity = med.toMedicationEntity()
        return medicationDao.deleteMedication(medicationEntity).toLong()
    }

    override suspend fun deleteAllMedications() {
        medicationDao.deleteAllMedications()
    }

    override suspend fun addAlarm(alarm: Alarm): Long? {
        return medicationDao.insertAlarm(alarm.toAlarmEntity())
    }

    override suspend fun deleteAlarm(alarm: Alarm): Int {
        return medicationDao.deleteAlarm(alarm.toAlarmEntity())
    }

    override suspend fun getAlarms(medicationId: Int): MedicationWithAlarmsDomain {
        return medicationDao.getMedicationWithAlarmsById(medicationId).toDomain()
    }
}