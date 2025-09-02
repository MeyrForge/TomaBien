package com.meyrforge.tomabien.medication_tracker.data

import com.meyrforge.tomabien.medication_tracker.domain.MedicationTrackerRepository
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import javax.inject.Inject

class MedicationTrackerRepositoryImpl @Inject constructor(private val medicationTrackerDao: MedicationTrackerDao) : MedicationTrackerRepository {
    override suspend fun saveMedicationTracker(tracker: MedicationTracker) {
        TODO("Not yet implemented")
    }

    override suspend fun editMedicationTracker(tracker: MedicationTracker) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllMedicationTrackers(): List<MedicationTracker>? {
        TODO("Not yet implemented")
    }

    override suspend fun getMedicationTrackerByDate(date: String): MedicationTracker? {
        TODO("Not yet implemented")
    }
}