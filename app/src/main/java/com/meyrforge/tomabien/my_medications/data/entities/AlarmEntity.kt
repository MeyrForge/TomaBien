package com.meyrforge.tomabien.my_medications.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.meyrforge.tomabien.common.alarm.Alarm

@Entity(
    tableName = "alarm_table", foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_owner_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val requestCode: Int = 0,
    @ColumnInfo(name = "medication_owner_id", index = true) val medicationOwnerId: Int,
    val hour: Int,
    val minute: Int,
)

fun AlarmEntity.toAlarm(): Alarm {
    return Alarm(requestCode, hour, minute, medicationOwnerId)
}

fun Alarm.toAlarmEntity(): AlarmEntity {
    return AlarmEntity(requestCode, hour, minute, ownerId)
}
