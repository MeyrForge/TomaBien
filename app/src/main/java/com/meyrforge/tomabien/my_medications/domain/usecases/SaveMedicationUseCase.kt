package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import javax.inject.Inject

class SaveMedicationUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(name: String, dosage: String, optional: Boolean): Boolean{
        val medication = Medication(null, name, dosage, optional)
        val result = repository.saveMedication(medication)
        return result != null
    }
}