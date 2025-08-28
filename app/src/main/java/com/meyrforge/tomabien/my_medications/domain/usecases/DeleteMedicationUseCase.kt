package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import javax.inject.Inject

class DeleteMedicationUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(med: Medication):Boolean{
        val result = repository.deleteMedication(med)
        return result != null
    }
}