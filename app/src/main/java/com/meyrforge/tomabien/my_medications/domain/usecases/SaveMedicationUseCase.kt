package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import javax.inject.Inject

class SaveMedicationUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(name: String, grammage: String, optional: Boolean): Boolean{
        val medication = Medication(null, name, grammage, 1f, optional)
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