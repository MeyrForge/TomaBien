package com.meyrforge.tomabien.my_medications.data

import com.meyrforge.tomabien.my_medications.data.entities.toMedicationEntity
import com.meyrforge.tomabien.my_medications.data.entities.toMedication
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
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

    override suspend fun editMedication(med: Medication): Long? {
        val medicationEntity = med.toMedicationEntity()
        return medicationDao.updateMedication(medicationEntity).toLong()
    }

    override suspend fun deleteMedication(med: Medication): Long? {
        val medicationEntity = med.toMedicationEntity()
        return medicationDao.deleteMedication(medicationEntity).toLong()
    }

    override suspend fun deleteAllMedications() {
        medicationDao.deleteAllMedications()
    }
}