package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import javax.inject.Inject

class DeleteAlarmUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.deleteAlarm(alarm)
    }
}