package com.meyrforge.tomabien.weekly_summary.domain.models

import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import com.meyrforge.tomabien.my_medications.domain.models.Medication

data class TrackerWithMedicationData(val tracker: MedicationTracker, val medication: Medication)
