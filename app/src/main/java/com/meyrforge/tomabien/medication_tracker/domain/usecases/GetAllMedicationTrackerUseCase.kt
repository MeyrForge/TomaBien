package com.meyrforge.tomabien.medication_tracker.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.medication_tracker.domain.MedicationTrackerRepository
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import javax.inject.Inject

class GetAllMedicationTrackerUseCase @Inject constructor(private val repository: MedicationTrackerRepository) {
    suspend operator fun invoke (): List<MedicationTracker>? {
        return when (val result = repository.getAllMedicationTrackers()){
            is Result.Success -> {
                result.data
            }

            is Result.Error -> {
                null
            }
        }
    }
}