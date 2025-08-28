package com.meyrforge.tomabien.my_medications.domain

import com.meyrforge.tomabien.my_medications.domain.models.Medication

interface MedicationRepository {
    suspend fun saveMedication(med: Medication): Long?
    suspend fun getAllMedications(): List<Medication>?
    suspend fun editMedication(med: Medication): Long?
    suspend fun deleteMedication(med: Medication): Long?
    suspend fun deleteAllMedications()
}