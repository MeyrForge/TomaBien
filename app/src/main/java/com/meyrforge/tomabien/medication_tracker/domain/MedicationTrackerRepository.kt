package com.meyrforge.tomabien.medication_tracker.domain

import com.meyrforge.tomabien.common.RepositoryError
import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker

interface MedicationTrackerRepository {
    suspend fun saveMedicationTracker(tracker: MedicationTracker): Result<Long, RepositoryError>
    suspend fun editMedicationTracker(tracker: MedicationTracker):Result<Int, RepositoryError>
    suspend fun getAllMedicationTrackers(): Result<List<MedicationTracker>, RepositoryError>
    suspend fun getMedicationTrackerByDate(date: String): Result<MedicationTracker?, RepositoryError>
}