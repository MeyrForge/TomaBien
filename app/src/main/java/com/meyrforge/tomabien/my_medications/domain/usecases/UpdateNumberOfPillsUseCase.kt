package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import javax.inject.Inject

class UpdateNumberOfPillsUseCase @Inject constructor(private val medicationRepository: MedicationRepository) {
    suspend operator fun invoke(medicationId: Int, numberOfPills: Float): Boolean {
        return when (medicationRepository.updateNumberOfPills(medicationId, numberOfPills)){
            is Result.Success -> {
                true
            }
            is Result.Error -> {
                false
            }
        }
    }

}