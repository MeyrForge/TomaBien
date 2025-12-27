package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import javax.inject.Inject

class SaveMedicationUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(name: String, grammage: String, optional: Boolean, numberOfPills: Float?, countActivated: Boolean): Int{
        val medication = Medication(null, name, grammage, optional, numberOfPills?: -1f, countActivated = countActivated)
        return when (val result = repository.saveMedication(medication)){
            is Result.Success -> {
                result.data.toInt()
            }

            is Result.Error -> {
                0
            }
        }
    }
}