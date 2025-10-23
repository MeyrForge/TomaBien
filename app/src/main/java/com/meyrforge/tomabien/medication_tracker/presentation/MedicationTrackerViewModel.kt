package com.meyrforge.tomabien.medication_tracker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meyrforge.tomabien.medication_tracker.domain.MedicationTrackerRepository
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import com.meyrforge.tomabien.medication_tracker.domain.usecases.GetAllMedicationTrackerByDateUseCase
import com.meyrforge.tomabien.medication_tracker.domain.usecases.GetAllMedicationTrackerUseCase
import com.meyrforge.tomabien.medication_tracker.domain.usecases.GetMedicationTrackerByIdUseCase
import com.meyrforge.tomabien.medication_tracker.domain.usecases.SaveMedicationTrackerUseCase
import com.meyrforge.tomabien.medication_tracker.domain.usecases.UpdateMedicationTrackerUseCase
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.domain.models.MedicationWithAlarmsDomain
import com.meyrforge.tomabien.my_medications.domain.usecases.GetAlarmsUseCase
import com.meyrforge.tomabien.my_medications.domain.usecases.GetAllMedicationsUseCase
import com.meyrforge.tomabien.my_medications.domain.usecases.UpdateNumberOfPillsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationTrackerViewModel @Inject constructor(
    private val saveMedicationTrackerUseCase: SaveMedicationTrackerUseCase,
    private val getAllMedicationsUseCase: GetAllMedicationsUseCase,
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val updateMedicationTrackerUseCase: UpdateMedicationTrackerUseCase,
    private val updateNumberOfPillsUseCase: UpdateNumberOfPillsUseCase,
    private val getAllMedicationTrackerByDateUseCase: GetAllMedicationTrackerByDateUseCase,
    private val getMedicationTrackerByIdUseCase: GetMedicationTrackerByIdUseCase,
) :
    ViewModel() {
    private val _medicationList = MutableLiveData(emptyList<MedicationWithAlarmsDomain>())
    val medicationList: LiveData<List<MedicationWithAlarmsDomain>> = _medicationList

    private val _wereSaved = mutableListOf<MedicationTracker>()
    val wereSaved = _wereSaved

    private val _medicationTrackerList = MutableLiveData(emptyList<MedicationTracker>())
    val medicationTrackerList: LiveData<List<MedicationTracker>?> = _medicationTrackerList

    init {
        getMedicationWithAlarms()
        getAllMedicationTrackers()
    }

    fun getAllMedicationTrackers() {
        viewModelScope.launch {
            _medicationTrackerList.value = getAllMedicationTrackerByDateUseCase()
        }
    }


    fun saveOrEditMedicationTracker(medId: Int, date: String, hour: String, taken: Boolean) {
        for (tracker in _wereSaved) {
            if (tracker.medicationId == medId && tracker.date == date && tracker.hour == hour) {
                viewModelScope.launch {
                    getMedicationTrackerByIdUseCase(medId)?.let {
                        it.taken = taken
                        updateMedicationTrackerUseCase(it)
                        val medicationToChange =
                            medicationList.value?.find { medication ->
                                medication.medication?.id == medId
                            }?.medication
                        if (medicationToChange != null && medicationToChange.numberOfPills != -1f) {
                            if (taken && medicationToChange.numberOfPills >= medicationToChange.dosage) {
                                updateNumberOfPillsUseCase(
                                    medId,
                                    (medicationToChange.numberOfPills - medicationToChange.dosage)
                                )
                                medicationList.value?.find { medication ->
                                    medication.medication?.id == medId
                                }?.medication?.let { med -> med.numberOfPills -= medicationToChange.dosage }
                            } else {
                                updateNumberOfPillsUseCase(
                                    medId,
                                    medicationToChange.numberOfPills + medicationToChange.dosage
                                )
                                medicationList.value?.find { medication ->
                                    medication.medication?.id == medId
                                }?.medication?.let { med -> med.numberOfPills += medicationToChange.dosage }
                            }
                        }
                        getAllMedicationTrackers()
                    }
                }
                return
            }
        }
        for (tracker in _medicationTrackerList.value ?: emptyList()) {
            if (tracker.medicationId == medId && tracker.date == date && tracker.hour == hour) {
                viewModelScope.launch {
                    tracker.id?.let { id ->
                        getMedicationTrackerByIdUseCase(id)?.let {
                            it.taken = taken
                            updateMedicationTrackerUseCase(it)
                            val medicationToChange =
                                medicationList.value?.find { medication ->
                                    medication.medication?.id == medId
                                }?.medication
                            if (medicationToChange != null && medicationToChange.numberOfPills != -1f) {
                                if (taken && medicationToChange.numberOfPills >= medicationToChange.dosage) {
                                    updateNumberOfPillsUseCase(
                                        medId,
                                        (medicationToChange.numberOfPills - medicationToChange.dosage)
                                    )
                                    medicationList.value?.find { medication ->
                                        medication.medication?.id == medId
                                    }?.medication?.let { med -> med.numberOfPills -= medicationToChange.dosage }
                                } else {
                                    updateNumberOfPillsUseCase(
                                        medId,
                                        medicationToChange.numberOfPills + medicationToChange.dosage
                                    )
                                    medicationList.value?.find { medication ->
                                        medication.medication?.id == medId
                                    }?.medication?.let { med -> med.numberOfPills += medicationToChange.dosage }
                                }
                            }
                            getAllMedicationTrackers()
                        }
                    }
                }
                return
            }
        }

        viewModelScope.launch {
            val id = saveMedicationTrackerUseCase(medId, date, hour, taken)
            id?.let {
                _wereSaved.add(
                    MedicationTracker(
                        date = date,
                        hour = hour,
                        medicationId = medId,
                        taken = taken,
                        id = it.toInt()
                    )
                )
                val medicationToChange = medicationList.value?.find { medication ->
                    medication.medication?.id == medId
                }?.medication
                if (medicationToChange != null && medicationToChange.numberOfPills != 1f && medicationToChange.numberOfPills >= medicationToChange.dosage && taken) {
                    updateNumberOfPillsUseCase(
                        medId,
                        (medicationToChange.numberOfPills - medicationToChange.dosage)
                    )
                    medicationList.value?.find { medication ->
                        medication.medication?.id == medId
                    }?.medication?.let { med -> med.numberOfPills -= medicationToChange.dosage }
                }
                getAllMedicationTrackers()
            }
        }
    }


    fun getMedicationWithAlarms() {
        viewModelScope.launch {
            val medications = getAllMedicationsUseCase()
            val medicationsThatHaveAlarms = mutableListOf<MedicationWithAlarmsDomain>()
            medications?.let {
                for (medication in medications) {
                    val result = getAlarmsUseCase(medication.id!!)
                    result?.let {
                        if (it.alarms.isNotEmpty() && !medication.deleted) {
                            medicationsThatHaveAlarms.add(it)
                        }
                    }
                }
            }
            _medicationList.value = medicationsThatHaveAlarms
        }
    }

}