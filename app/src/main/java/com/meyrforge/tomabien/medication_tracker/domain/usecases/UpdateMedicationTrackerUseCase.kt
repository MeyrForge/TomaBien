package com.meyrforge.tomabien.medication_tracker.domain.usecases

import com.meyrforge.tomabien.medication_tracker.domain.MedicationTrackerRepository
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import javax.inject.Inject

class UpdateMedicationTrackerUseCase @Inject constructor(private val repository: MedicationTrackerRepository) {
    suspend operator fun invoke(tracker: MedicationTracker){
        repository.editMedicationTracker(tracker)
    }
}