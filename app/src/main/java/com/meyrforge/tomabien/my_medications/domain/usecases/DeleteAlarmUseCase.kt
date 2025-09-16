package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import javax.inject.Inject

class DeleteAlarmUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(alarm: Alarm):Boolean {
        return when (repository.deleteAlarm(alarm)){
            is Result.Success -> {
                true
            }

            is Result.Error -> {
                false
            }
        }
    }
}