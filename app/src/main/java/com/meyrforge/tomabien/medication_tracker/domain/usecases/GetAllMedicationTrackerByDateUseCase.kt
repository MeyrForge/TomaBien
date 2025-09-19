package com.meyrforge.tomabien.medication_tracker.domain.usecases

import com.meyrforge.tomabien.common.Result
import com.meyrforge.tomabien.medication_tracker.domain.MedicationTrackerRepository
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import java.util.Calendar
import javax.inject.Inject

class GetAllMedicationTrackerByDateUseCase @Inject constructor(private val repository: MedicationTrackerRepository) {
    suspend operator fun invoke(): List<MedicationTracker>?{
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val formattedDay = day.toString().padStart(2, '0')
        val formattedMonth = month.toString().padStart(2, '0')

        val date = "$formattedDay/$formattedMonth/$year"

        return when (val result = repository.getMedicationTrackerByDate(date)){
            is Result.Success -> {
                result.data
            }

            is Result.Error -> {
                null
            }
        }
    }
}