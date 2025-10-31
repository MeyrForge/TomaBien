package com.meyrforge.tomabien.medication_tracker.domain.models

data class MedicationTracker(
    val id: Int?,
    val date: String,
    val hour: String,
    val medicationId: Int,
    var taken: Boolean,
    var numberOfPills: Float = 0.0f,
    var lastTimeWasExtracted: Boolean = false
)
