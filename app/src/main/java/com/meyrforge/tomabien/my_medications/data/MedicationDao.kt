package com.meyrforge.tomabien.my_medications.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.meyrforge.tomabien.my_medications.data.entities.AlarmEntity
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

    @Transaction
    @Query("SELECT * FROM medication_table WHERE id = :medicationId")
    suspend fun getMedicationWithAlarmsById(medicationId: Int): MedicationEntity?

    @Transaction
    @Query("SELECT * FROM alarm_table WHERE medication_owner_id = :medicationId")
    suspend fun getAlarms(medicationId: Int): List<AlarmEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAlarm(alarm: AlarmEntity): Long?

    @Delete
    suspend fun deleteAlarm(alarm: AlarmEntity) : Int

}