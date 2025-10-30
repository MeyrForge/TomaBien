package com.meyrforge.tomabien.my_medications.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.meyrforge.tomabien.my_medications.domain.models.Medication

@Entity(tableName = "medication_table")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "medication_name") val medicationName: String,
    @ColumnInfo(name = "medication_grammage") val medicationGrammage: String,
    @ColumnInfo(name = "number_of_pills", defaultValue = "-1.0")
    val numberOfPills: Float = -1.0f,
    @ColumnInfo(name = "optional") val optional: Boolean,
    @ColumnInfo(name = "deleted") val deleted: Boolean,
    @ColumnInfo(name = "count_activated") val countActivated: Boolean = false

)

fun Medication.toMedicationEntity(): MedicationEntity{
    return if(id != null) {
        MedicationEntity(
            id = id,
            medicationName = name,
            optional = optional,
            deleted = deleted,
            medicationGrammage = grammage,
            numberOfPills = numberOfPills,
            countActivated = countActivated
        )
    } else {
        MedicationEntity(
            medicationName = name,
            optional = optional,
            deleted = deleted,
            medicationGrammage = grammage,
            numberOfPills = numberOfPills,
            countActivated = countActivated
        )
    }
}

fun MedicationEntity.toMedication(): Medication{
    return Medication(
        id = id,
        name = medicationName,
        optional = optional,
        deleted = deleted,
        grammage = medicationGrammage,
        numberOfPills = numberOfPills,
        countActivated = countActivated
    )
}