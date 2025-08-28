package com.meyrforge.tomabien.my_medications.domain.models

data class Medication(
    val id: Int?,
    val name: String,
    val dosage: String,
    val optional: Boolean,
    var taken: Boolean = false)
