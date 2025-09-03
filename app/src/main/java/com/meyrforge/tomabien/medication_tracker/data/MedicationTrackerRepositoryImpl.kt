package com.meyrforge.tomabien.medication_tracker.data

import com.meyrforge.tomabien.medication_tracker.data.entities.toDomain
import com.meyrforge.tomabien.medication_tracker.data.entities.toEntity
import com.meyrforge.tomabien.medication_tracker.domain.MedicationTrackerRepository
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import javax.inject.Inject

class MedicationTrackerRepositoryImpl @Inject constructor(private val medicationTrackerDao: MedicationTrackerDao) : MedicationTrackerRepository {
    override suspend fun saveMedicationTracker(tracker: MedicationTracker): Long? {
        return medicationTrackerDao.insertMedicationTracker(tracker.toEntity())
    }

    override suspend fun editMedicationTracker(tracker: MedicationTracker): Int {
        return medicationTrackerDao.updateMedicationTracker(tracker.toEntity())
    }

    override suspend fun getAllMedicationTrackers(): List<MedicationTracker>? {
        return medicationTrackerDao.getAllMedicationTrackers()?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun getMedicationTrackerByDate(date: String): MedicationTracker? {
        return medicationTrackerDao.getMedicationTrackerByDate(date)?.toDomain()
    }
}