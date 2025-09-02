package com.meyrforge.tomabien.medication_tracker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import com.meyrforge.tomabien.medication_tracker.domain.usecases.SaveMedicationTrackerUseCase
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.domain.models.MedicationWithAlarmsDomain
import com.meyrforge.tomabien.my_medications.domain.usecases.GetAlarmsUseCase
import com.meyrforge.tomabien.my_medications.domain.usecases.GetAllMedicationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationTrackerViewModel @Inject constructor(
    private val saveMedicationTrackerUseCase: SaveMedicationTrackerUseCase,
    private val getAllMedicationsUseCase: GetAllMedicationsUseCase,
    private val getAlarmsUseCase: GetAlarmsUseCase
) :
    ViewModel() {
    private val _medicationList = MutableLiveData(emptyList<MedicationWithAlarmsDomain>())
    val medicationList: LiveData<List<MedicationWithAlarmsDomain>> = _medicationList

    init {
        getMedicationWithAlarms()
    }

    fun saveMedicationTracker(medId: Int, date: String, hour: String) {
        viewModelScope.launch {
            saveMedicationTrackerUseCase(medId, date, hour)
        }
    }

    private fun getMedicationWithAlarms() {
        viewModelScope.launch {
            val medications = getAllMedicationsUseCase()
            val medicationsThatHaveAlarms = mutableListOf<MedicationWithAlarmsDomain>()
            medications?.let {
                for (medication in medications) {
                    val result = getAlarmsUseCase(medication.id!!)
                    if (result.alarms.isNotEmpty()){
                        medicationsThatHaveAlarms.add(result)
                    }
                }
            }
            _medicationList.value = medicationsThatHaveAlarms
        }
    }
}