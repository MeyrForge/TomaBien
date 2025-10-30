package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import javax.inject.Inject

class SaveMedicationUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(name: String, grammage: String, optional: Boolean, numberOfPills: Float = -1f, countActivated: Boolean): Boolean{
        val medication = Medication(null, name, grammage, optional, numberOfPills, countActivated = countActivated)
        return when (val result = repository.saveMedication(medication)){
            is Result.Success -> {
                true
            }

            is Result.Error -> {
                false
            }
        }
    }
}