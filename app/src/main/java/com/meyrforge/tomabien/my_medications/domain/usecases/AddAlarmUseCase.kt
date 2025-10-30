package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import javax.inject.Inject

class AddAlarmUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(requestCode: Int, hour:Int, minute:Int, medicationId: Int, dosage: Float):Boolean {
        return when (val result = repository.addAlarm(Alarm(requestCode, hour, minute, medicationId, dosage))) {
            is Result.Success -> {
                result.data
                true
            }

            is Result.Error -> {
                false
            }
        }
    }
}