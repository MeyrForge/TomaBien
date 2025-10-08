package com.meyrforge.tomabien.my_medications.domain.models

data class Medication(
    val id: Int?,
    val name: String,
    val grammage: String,
    val dosage: Float,
    val optional: Boolean,
    val numberOfPills: Float = -1.0f,
    var taken: Boolean = false,
    var deleted: Boolean = false)
