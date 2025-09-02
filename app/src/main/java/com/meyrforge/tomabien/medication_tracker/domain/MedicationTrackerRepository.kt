package com.meyrforge.tomabien.medication_tracker.domain

import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker

interface MedicationTrackerRepository {
    suspend fun saveMedicationTracker(tracker: MedicationTracker)
    suspend fun editMedicationTracker(tracker: MedicationTracker)
    suspend fun getAllMedicationTrackers(): List<MedicationTracker>?
    suspend fun getMedicationTrackerByDate(date: String): MedicationTracker?
}