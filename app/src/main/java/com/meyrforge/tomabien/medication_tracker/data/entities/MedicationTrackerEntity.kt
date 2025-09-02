package com.meyrforge.tomabien.medication_tracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.meyrforge.tomabien.my_medications.data.entities.AlarmEntity
import com.meyrforge.tomabien.my_medications.data.entities.MedicationEntity

@Entity(tableName = "medication_tracker_table", foreignKeys = [
    ForeignKey(
        entity = MedicationEntity::class,
        parentColumns = ["id"],
        childColumns = ["taken_medication_id"],
        onDelete = ForeignKey.CASCADE
    )
])
data class MedicationTrackerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "hour") val hour: String,
    @ColumnInfo(name = "taken_medication_id") val takenMedicationId: Int
)

data class MedicationWithTrackers(
    @Embedded
    val medication: MedicationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "taken_medication_id"
    )
    val alarms: List<MedicationTrackerEntity>
)
