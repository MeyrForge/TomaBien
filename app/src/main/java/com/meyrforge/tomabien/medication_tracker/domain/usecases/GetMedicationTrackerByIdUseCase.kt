package com.meyrforge.tomabien.medication_tracker.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.medication_tracker.domain.MedicationTrackerRepository
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import javax.inject.Inject

class GetMedicationTrackerByIdUseCase @Inject constructor(private val repository: MedicationTrackerRepository) {
    suspend operator fun invoke(id:Int): MedicationTracker?{
        return when (val result = repository.getMedicationTrackerById(id)){
            is Result.Success -> {
                result.data
            }

            is Result.Error -> {
                null
            }
        }
    }
}