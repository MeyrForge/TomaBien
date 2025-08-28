package com.meyrforge.tomabien.my_medications.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.meyrforge.tomabien.my_medications.data.entities.MedicationEntity

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMedication(med: MedicationEntity) : Long?

    @Update
    suspend fun updateMedication(med: MedicationEntity) : Int

    @Delete
    suspend fun deleteMedication(med: MedicationEntity) : Int

    @Query("SELECT * FROM medication_table")
    suspend fun getAllMedications(): List<MedicationEntity>?

    @Query("DELETE FROM medication_table")
    suspend fun deleteAllMedications()
}