package com.meyrforge.tomabien.my_medications.domain.usecases

import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import com.meyrforge.tomabien.my_medications.domain.models.MedicationWithAlarmsDomain
import javax.inject.Inject

class GetAlarmsUseCase @Inject constructor(private val repository: MedicationRepository) {
    suspend operator fun invoke(medicationId: Int): MedicationWithAlarmsDomain{
        return repository.getAlarms(medicationId)
    }
}