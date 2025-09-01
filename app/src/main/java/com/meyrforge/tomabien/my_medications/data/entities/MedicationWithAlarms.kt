package com.meyrforge.tomabien.my_medications.data.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.meyrforge.tomabien.my_medications.domain.models.MedicationWithAlarmsDomain

data class MedicationWithAlarms(
    @Embedded
    val medication: MedicationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "medication_owner_id"
    )
    val alarms: List<AlarmEntity>
)

fun MedicationWithAlarms.toDomain(): MedicationWithAlarmsDomain {
    return MedicationWithAlarmsDomain(
        medication.id,
        alarms.map { it.toAlarm() },
        medication.toMedication()
    )
}
