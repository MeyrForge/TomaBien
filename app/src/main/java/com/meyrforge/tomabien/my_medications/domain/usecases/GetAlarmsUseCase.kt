package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.MedicationWithAlarmsDomain
import javax.inject.Inject

class GetAlarmsUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(medicationId: Int): MedicationWithAlarmsDomain?{
        return when (val result = repository.getAlarms(medicationId)){
            is Result.Success -> {
                result.data
            }

            is Result.Error -> {
                null
            }
        }
    }
}