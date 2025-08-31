package com.meyrforge.tomabien.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meyrforge.tomabien.my_medications.data.MedicationDao
import com.meyrforge.tomabien.my_medications.data.entities.AlarmEntity
import com.meyrforge.tomabien.my_medications.data.entities.MedicationEntity

@Database(
    entities = [MedicationEntity::class, AlarmEntity::class],
    version = 2
)
abstract class TomaBienDatabase : RoomDatabase(){
    abstract fun medicationDao(): MedicationDao
}