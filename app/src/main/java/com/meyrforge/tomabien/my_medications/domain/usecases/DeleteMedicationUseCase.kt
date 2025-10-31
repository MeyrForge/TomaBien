package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.my_medications.domain.AlarmScheduler
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import javax.inject.Inject

class DeleteMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(med: Medication): Boolean {
        val result = repository.getAlarms(med.id ?: 0)
        if (result is Result.Success) {
            result.data.alarms.forEach { alarm ->
                alarmScheduler.cancel(alarm.requestCode, med.name)
                repository.deleteAlarm(alarm)
            }
        }

        return when (repository.deleteMedication(med)) {
            is Result.Success -> {
                true
            }

            is Result.Error -> {
                false
            }
        }
    }
}