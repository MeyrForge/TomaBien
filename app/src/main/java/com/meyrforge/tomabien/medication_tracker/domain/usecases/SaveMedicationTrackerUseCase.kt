package com.meyrforge.tomabien.medication_tracker.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.medication_tracker.domain.MedicationTrackerRepository
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import javax.inject.Inject

class SaveMedicationTrackerUseCase @Inject constructor(private val repository: MedicationTrackerRepository) {
    suspend operator fun invoke(medId: Int, date: String, hour: String, taken: Boolean):Long?{
        val tracker = MedicationTracker(null, date, hour, medId, taken)
        return when (val result = repository.saveMedicationTracker(tracker)){
            is Result.Success -> {
                result.data
            }

            is Result.Error -> {
                null
            }
        }
    }
}