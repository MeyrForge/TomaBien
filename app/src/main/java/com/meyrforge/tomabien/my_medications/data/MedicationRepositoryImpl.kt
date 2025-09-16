package com.meyrforge.tomabien.my_medications.data

import android.database.sqlite.SQLiteException
import android.util.Log
import com.meyrforge.tomabien.common.RepositoryError
import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.data.entities.toAlarm
import com.meyrforge.tomabien.my_medications.data.entities.toAlarmEntity
import com.meyrforge.tomabien.my_medications.data.entities.toMedication
import com.meyrforge.tomabien.my_medications.data.entities.toMedicationEntity
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.domain.models.MedicationWithAlarmsDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(private val medicationDao: MedicationDao) :
    MedicationRepository {
    private suspend fun <T> safeDaoCall(daoCall: suspend () -> T): Result<T, RepositoryError> {
        return try {
            Result.Success(withContext(Dispatchers.IO) { daoCall() })
        } catch (e: SQLiteException) {
            Log.e("MedicationRepository", "Database error", e)
            Result.Error(RepositoryError.DatabaseError)
        } catch (e: Exception) {
            Log.e("MedicationRepository", "Unknown error", e)
            Result.Error(RepositoryError.UnknownError(e.message))
        }
    }
    override suspend fun saveMedication(med: Medication): Result<Long, RepositoryError> {
        return safeDaoCall {
            val medicationEntity = med.toMedicationEntity()
            medicationDao.insertMedication(medicationEntity)
                ?: throw IllegalStateException("Insert returned null, expected a Long row ID")
        }
    }

    override suspend fun getAllMedications(): Result<List<Medication>, RepositoryError> {
        return safeDaoCall {
            medicationDao.getAllMedications()?.map { it.toMedication() } ?: emptyList()
        }
    }

    override suspend fun editMedication(med: Medication): Result<Long, RepositoryError> {
        return safeDaoCall {
            val medicationEntity = med.toMedicationEntity()
            val updatedRows = medicationDao.updateMedication(medicationEntity)
            if (updatedRows == 0) {
                Result.Error(RepositoryError.MedicationNotFound)
            }
            updatedRows.toLong()
        }
    }

    override suspend fun deleteMedication(med: Medication): Result<Long, RepositoryError> {
        return safeDaoCall {
            val medicationEntity = med.toMedicationEntity()
            val deletedRows = medicationDao.deleteMedication(medicationEntity)
            if (deletedRows == 0) {
                Result.Error(RepositoryError.MedicationNotFound)
            }
            deletedRows.toLong()
        }
    }

    override suspend fun deleteAllMedications(): Result<Unit, RepositoryError> {
        return safeDaoCall {
            medicationDao.deleteAllMedications()
        }
    }

    override suspend fun addAlarm(alarm: Alarm): Result<Long, RepositoryError> {
        return safeDaoCall {
            medicationDao.insertAlarm(alarm.toAlarmEntity())
                ?: throw IllegalStateException("Insert alarm returned null")
        }
    }

    override suspend fun deleteAlarm(alarm: Alarm): Result<Int, RepositoryError> {
        return safeDaoCall {
            medicationDao.deleteAlarm(alarm.toAlarmEntity())
        }
    }

    override suspend fun getAlarms(medicationId: Int): Result<MedicationWithAlarmsDomain, RepositoryError> {
        try {
            val medicationEntity = withContext(Dispatchers.IO) { medicationDao.getMedicationWithAlarmsById(medicationId) }
            if (medicationEntity == null) {
                return Result.Error(RepositoryError.MedicationNotFound)
            }
            val alarms = withContext(Dispatchers.IO) { medicationDao.getAlarms(medicationId) }
            val domainObject = MedicationWithAlarmsDomain(
                medicationId,
                alarms.map { it.toAlarm() },
                medicationEntity.toMedication()
            )
            return Result.Success(domainObject)
        } catch (e: SQLiteException) {
            return Result.Error(RepositoryError.DatabaseError)
        } catch (e: Exception) {
            return Result.Error(RepositoryError.UnknownError(e.message))
        }
    }

    override suspend fun getMedicationById(medicationId: Int): Result<Medication?, RepositoryError> {
        return safeDaoCall {
            // Aquí, si el medicamento no se encuentra, `toMedication()` podría fallar si `getMedicationWithAlarmsById` devuelve null
            // y la función `toMedication` espera un objeto no nulo.
            // Es mejor manejar el null directamente.
            val medicationEntity = medicationDao.getMedicationWithAlarmsById(medicationId)
            medicationEntity?.toMedication() // Devolverá null si medicationEntity es null, lo cual es el comportamiento deseado.
        }
    }

}