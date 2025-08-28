package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import javax.inject.Inject

class GetAllMedicationsUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(): List<Medication>?{
        return repository.getAllMedications()
    }
}