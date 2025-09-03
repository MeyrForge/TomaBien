package com.meyrforge.tomabien.medication_tracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import com.meyrforge.tomabien.my_medications.data.entities.AlarmEntity
import com.meyrforge.tomabien.my_medications.data.entities.MedicationEntity

@Entity(tableName = "medication_tracker_table", foreignKeys = [
    ForeignKey(
        entity = MedicationEntity::class,
        parentColumns = ["id"],
        childColumns = ["medication_id"],
        onDelete = ForeignKey.CASCADE
    )
])
data class MedicationTrackerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "hour") val hour: String,
    @ColumnInfo(name = "medication_id") val medicationId: Int,
    @ColumnInfo(name = "taken") val taken: Boolean
)

data class MedicationWithTrackers(
    @Embedded
    val medication: MedicationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "medication_id"
    )
    val alarms: List<MedicationTrackerEntity>
)

fun MedicationTrackerEntity.toDomain(): MedicationTracker{
    return MedicationTracker(
        id = id,
        date = date,
        hour = hour,
        medicationId = medicationId,
        taken = taken
    )
}

fun MedicationTracker.toEntity(): MedicationTrackerEntity{
    return MedicationTrackerEntity(
        date = date,
        hour = hour,
        medicationId = medicationId,
        taken = taken
    )
}