package com.meyrforge.tomabien.medication_tracker.data

import android.database.sqlite.SQLiteException
import android.util.Log
import com.meyrforge.tomabien.common.RepositoryError
import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.medication_tracker.data.entities.toDomain
import com.meyrforge.tomabien.medication_tracker.data.entities.toEntity
import com.meyrforge.tomabien.medication_tracker.domain.MedicationTrackerRepository
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MedicationTrackerRepositoryImpl @Inject constructor(private val medicationTrackerDao: MedicationTrackerDao) : MedicationTrackerRepository {
    private suspend fun <T> safeDaoCall(daoCall: suspend () -> T): Result<T, RepositoryError> {
        return try {
            Result.Success(withContext(Dispatchers.IO) { daoCall() })
        } catch (e: SQLiteException) {
            Log.e("MedicationTrackerRepository", "Database error", e)
            Result.Error(RepositoryError.DatabaseError)
        } catch (e: Exception) {
            Log.e("MedicationTrackerRepository", "Unknown error", e)
            Result.Error(RepositoryError.UnknownError(e.message))
        }
    }

    override suspend fun saveMedicationTracker(tracker: MedicationTracker): Result<Long, RepositoryError> {
        return safeDaoCall {
            val trackerEntity = tracker.toEntity()
            medicationTrackerDao.insertMedicationTracker(trackerEntity)
                ?: throw IllegalStateException("Insert returned null, expected a Long row ID")
        }
    }

    override suspend fun editMedicationTracker(tracker: MedicationTracker): Result<Int, RepositoryError> {
        return safeDaoCall {
            val trackerEntity = tracker.toEntity()
            val updatedRows = medicationTrackerDao.updateMedicationTracker(trackerEntity)
            if (updatedRows == 0) {
                Result.Error(RepositoryError.MedicationNotFound)
            }
            updatedRows
        }
    }

    override suspend fun getAllMedicationTrackers(): Result<List<MedicationTracker>, RepositoryError> {
        return safeDaoCall {
            medicationTrackerDao.getAllMedicationTrackers()?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun getMedicationTrackerByDate(date: String): Result<MedicationTracker?, RepositoryError> {
        return safeDaoCall {
            medicationTrackerDao.getMedicationTrackerByDate(date)?.toDomain()
        }
    }
}