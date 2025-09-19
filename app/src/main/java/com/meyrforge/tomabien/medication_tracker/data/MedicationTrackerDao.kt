package com.meyrforge.tomabien.medication_tracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.meyrforge.tomabien.medication_tracker.data.entities.MedicationTrackerEntity
import com.meyrforge.tomabien.my_medications.data.entities.MedicationEntity

@Dao
interface MedicationTrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicationTracker(medicationTracker: MedicationTrackerEntity): Long?

    @Query("SELECT * FROM medication_tracker_table")
    suspend fun getAllMedicationTrackers(): List<MedicationTrackerEntity>?

    @Query("SELECT * FROM medication_tracker_table WHERE date = :date")
    suspend fun getMedicationTrackerByDate(date: String): List<MedicationTrackerEntity>?

    @Update
    suspend fun updateMedicationTracker(medicationTracker: MedicationTrackerEntity) : Int

    @Query("SELECT * FROM medication_tracker_table WHERE id = :id")
    suspend fun getMedicationTrackerById(id:Int): MedicationTrackerEntity?

}