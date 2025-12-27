package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import javax.inject.Inject

class EditMedicationUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(med: Medication): Long{
        return when (val result = repository.editMedication(med)) {
            is Result.Success -> {
                result.data
            }

            is Result.Error -> {
                0L
            }
        }
    }
}