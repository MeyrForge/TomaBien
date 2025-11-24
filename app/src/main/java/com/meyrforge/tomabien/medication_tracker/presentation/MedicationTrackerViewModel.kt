package com.meyrforge.tomabien.medication_tracker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meyrforge.tomabien.common.formatHour
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import com.meyrforge.tomabien.medication_tracker.domain.usecases.GetAllMedicationTrackerByDateUseCase
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

    private val _trackersToSaveOrEdit = MutableLiveData(emptyList<MedicationTracker>())
    val trackersToSaveOrEdit: LiveData<List<MedicationTracker>> = _trackersToSaveOrEdit

    private val _notificationMessage = MutableLiveData<String?>(null)
    val notificationMessage: LiveData<String?> = _notificationMessage

    init {
        getMedicationWithAlarms()
        getAllMedicationTrackers()
    }

    fun getAllMedicationTrackers() {
        viewModelScope.launch {
            _medicationTrackerList.value = getAllMedicationTrackerByDateUseCase()
        }
    }

    fun addTrackerToList(tracker: MedicationTracker) {
        _trackersToSaveOrEdit.value?.find { it.medicationId == tracker.medicationId && it.date == tracker.date && it.hour == tracker.hour }
            ?.apply {
                taken = tracker.taken
                return
            }
        _trackersToSaveOrEdit.value = _trackersToSaveOrEdit.value?.plus(tracker)
    }

    fun saveTrackers() {
        viewModelScope.launch {
            getAllMedicationTrackers()
            //si ya hay trackers guardados para esta fecha
            if (!_medicationTrackerList.value.isNullOrEmpty()) {
                //recorro la lista de trackers a guardar
                for (trackerToSave in _trackersToSaveOrEdit.value ?: emptyList()) {
                    //busco en la lista de trackers ya guardados el tracker a guardar
                    val savedTracker =
                        _medicationTrackerList.value?.find { it.medicationId == trackerToSave.medicationId && it.date == trackerToSave.date && it.hour == trackerToSave.hour }
                    //si el tracker a guardar ya esta en la lista de trackers guardados, lo edito en lugar de insertarlo
                    if (savedTracker != null) {
                        if (savedTracker.taken != trackerToSave.taken) {
                            savedTracker.taken = trackerToSave.taken
                            //busco si la medicacion tiene el conteo activado
                            medicationList.value?.find { medication ->
                                medication.medication?.id == trackerToSave.medicationId
                            }?.medication?.let { medicationToChangePillCount ->
                                if (medicationToChangePillCount.countActivated) {
                                    ifCountIsActiveUpdateNumberOfPills(
                                        savedTracker,
                                        medicationToChangePillCount,
                                        false
                                    )
                                }
                            }
                        }
                        //si el tracker a guardar no esta en la lista de trackers ya guardados lo inserto
                    } else {
                        medicationList.value?.find { medication ->
                            medication.medication?.id == trackerToSave.medicationId
                        }?.medication?.let { medicationToChangePillCount ->
                            if (medicationToChangePillCount.countActivated) {
                                ifCountIsActiveUpdateNumberOfPills(
                                    trackerToSave,
                                    medicationToChangePillCount,
                                    true
                                )
                            } else {
                                saveOrEditTracker(trackerToSave, true)
                            }
                        }
                    }
                }
                getAllMedicationTrackers()
                //si no hay trackers guardados para este dia
            } else {
                //recorro los trackers a guardar
                for (trackerToSave in _trackersToSaveOrEdit.value ?: emptyList()) {
                    medicationList.value?.find { medication ->
                        medication.medication?.id == trackerToSave.medicationId
                    }?.medication?.let { medicationToChangePillCount ->
                        if (medicationToChangePillCount.countActivated) {
                            ifCountIsActiveUpdateNumberOfPills(
                                trackerToSave,
                                medicationToChangePillCount,
                                true
                            )
                        } else {
                            saveOrEditTracker(trackerToSave, true)
                        }
                    }
                }
                getAllMedicationTrackers()
            }
        }
        _notificationMessage.value = "Seguimiento actualizado"
    }

    private suspend fun ifCountIsActiveUpdateNumberOfPills(
        trackerToSave: MedicationTracker,
        medicationToChangePillCount: Medication,
        isFirstTime: Boolean,
    ) {
        val alarms =
            medicationList.value?.find { medicationWithAlarms ->
                medicationWithAlarms.medication?.id == trackerToSave.medicationId
            }?.alarms
        val dosage =
            alarms?.find { alarm ->
                formatHour(
                    alarm.hour,
                    alarm.minute
                ) == trackerToSave.hour
            }?.dosage
        dosage?.let { dosage ->
            if (trackerToSave.taken && medicationToChangePillCount.numberOfPills >= dosage) {
                if (isFirstTime) {
                    when (trackerToSave.lastTimeWasExtracted) {
                        true -> trackerToSave.numberOfPills =
                            medicationToChangePillCount.numberOfPills

                        false -> {
                            trackerToSave.numberOfPills =
                                medicationToChangePillCount.numberOfPills - dosage
                            trackerToSave.lastTimeWasExtracted = true
                        }
                    }
                }
                saveOrEditTracker(trackerToSave, isFirstTime)
                updateNumberOfPillsUseCase(
                    trackerToSave.medicationId,
                    (medicationToChangePillCount.numberOfPills - dosage)
                )
                medicationList.value?.find { medication ->
                    medication.medication?.id == trackerToSave.medicationId
                }?.medication?.let { med -> med.numberOfPills = trackerToSave.numberOfPills }
            } else if (!trackerToSave.taken && !isFirstTime) {
                if (trackerToSave.lastTimeWasExtracted) {
                    trackerToSave.numberOfPills = medicationToChangePillCount.numberOfPills + dosage
                    trackerToSave.lastTimeWasExtracted = false
                }
                saveOrEditTracker(trackerToSave, false)
                updateNumberOfPillsUseCase(
                    trackerToSave.medicationId,
                    medicationToChangePillCount.numberOfPills + dosage
                )
                medicationList.value?.find { medication ->
                    medication.medication?.id == trackerToSave.medicationId
                }?.medication?.let { med -> med.numberOfPills = trackerToSave.numberOfPills }
            } else if (trackerToSave.taken && medicationToChangePillCount.numberOfPills < dosage) {
                trackerToSave.numberOfPills = medicationToChangePillCount.numberOfPills
                saveOrEditTracker(trackerToSave, isFirstTime)
            }
        }
    }

    private suspend fun saveOrEditTracker(
        trackerToSave: MedicationTracker,
        isFirstTime: Boolean,
    ) {
        if (isFirstTime) {
            saveMedicationTrackerUseCase(
                trackerToSave.medicationId,
                trackerToSave.date,
                trackerToSave.hour,
                trackerToSave.taken,
                trackerToSave.numberOfPills,
                trackerToSave.lastTimeWasExtracted
            )
        } else {
            updateMedicationTrackerUseCase(trackerToSave)
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

    fun clearMessage() {
        _notificationMessage.value = null
    }
}