package com.meyrforge.tomabien.weekly_summary.domain.usecases

import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import javax.inject.Inject

class GetMedicationByIdUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(id: Int): Medication?{
        return repository.getMedicationById(id)
    }
}