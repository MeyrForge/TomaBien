package com.meyrforge.tomabien.my_medications.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.meyrforge.tomabien.my_medications.domain.models.Medication

@Entity(tableName = "medication_table")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "medication_name") val medicationName: String,
    @ColumnInfo(name = "medication_dosage") val medicationDosage: String,
    @ColumnInfo(name = "optional") val optional: Boolean,
    @ColumnInfo(name = "deleted") val deleted: Boolean
)

fun Medication.toMedicationEntity(): MedicationEntity{
    return if(id != null) {
        MedicationEntity(
            id = id,
            medicationName = name,
            medicationDosage = dosage,
            optional = optional,
            deleted = deleted
        )
    } else {
        MedicationEntity(
            medicationName = name,
            medicationDosage = dosage,
            optional = optional,
            deleted = deleted
        )
    }
}

fun MedicationEntity.toMedication(): Medication{
    return Medication(
        id = id,
        name = medicationName,
        dosage = medicationDosage,
        optional = optional,
        deleted = deleted
    )
}