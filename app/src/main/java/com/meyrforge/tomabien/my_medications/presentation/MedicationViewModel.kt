package com.meyrforge.tomabien.my_medications.presentation

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meyrforge.tomabien.common.Constants
import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.domain.usecases.AddAlarmUseCase
import com.meyrforge.tomabien.my_medications.domain.usecases.DeleteAlarmUseCase
import com.meyrforge.tomabien.my_medications.domain.usecases.DeleteMedicationUseCase
import com.meyrforge.tomabien.my_medications.domain.usecases.EditMedicationUseCase
import com.meyrforge.tomabien.my_medications.domain.usecases.GetAlarmsUseCase
import com.meyrforge.tomabien.my_medications.domain.usecases.GetAllMedicationsUseCase
import com.meyrforge.tomabien.my_medications.domain.usecases.SaveMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveMedicationUseCase: SaveMedicationUseCase,
    private val getAllMedicationsUseCase: GetAllMedicationsUseCase,
    private val editMedicationUseCase: EditMedicationUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase,
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val addAlarmUseCase: AddAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase
) :
    ViewModel() {
    private val _medicationId = mutableIntStateOf(0)
    val medicationId = _medicationId

    private val _medicationName = mutableStateOf("")
    val medicationName = _medicationName

    private val _medicationDosage = mutableStateOf("")
    val medicationDosage = _medicationDosage

    private val _isOptional = mutableStateOf(false)
    val isOptional = _isOptional

    private val _medicationList = MutableLiveData(emptyList<Medication>())
    val medicationList = _medicationList

    private val _notificationMessage = MutableLiveData<String?>(null)
    val notificationMessage: LiveData<String?> = _notificationMessage

    private val _alarms = MutableLiveData(emptyList<Alarm>())
    val alarms = _alarms

    init {
        getAllMedications()
        savedStateHandle.get<String>(Constants.MEDICATION_ID)
            ?.let {
                _medicationId.intValue = it.toInt()
                getAlarms(it.toInt())
            }

    }

    fun onMedicationIdChange(id: Int) {
        _medicationId.intValue = id
    }

    fun onMedicationNameChange(name: String) {
        _medicationName.value = name
    }

    fun onMedicationDosageChange(dosage: String) {
        _medicationDosage.value = dosage
    }

    fun onIsOptionalChange(isOptional: Boolean) {
        _isOptional.value = isOptional
    }

    fun saveMedication() {
        viewModelScope.launch {
            val result = saveMedicationUseCase(
                _medicationName.value,
                _medicationDosage.value,
                _isOptional.value
            )
            if (result) {
                _medicationName.value = ""
                _medicationDosage.value = ""
                _isOptional.value = false
                getAllMedications()
                _notificationMessage.value = "Medicacion agregada"
            } else {
                _notificationMessage.value = "Algo salio mal"
            }
        }
    }

    private fun getAllMedications() {
        viewModelScope.launch {
            val medList = getAllMedicationsUseCase()
            if (medList != null)
                _medicationList.value = medList
        }
    }

    fun editMedication() {
        viewModelScope.launch {
            val result = editMedicationUseCase(
                Medication(
                    medicationId.intValue,
                    medicationName.value,
                    medicationDosage.value,
                    isOptional.value
                )
            )
            if (result) {
                getAllMedications()
            } else {
                _notificationMessage.value = "Algo salio mal"
            }
        }
    }

    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            val result = deleteMedicationUseCase(medication)
            if (result) {
                getAllMedications()
            } else {
                _notificationMessage.value = "Algo salio mal"
            }
        }
    }

    fun clearMessage() {
        _notificationMessage.value = null
    }

    fun addAlarm(requestCode: Int, hour: Int, minute: Int) {
        viewModelScope.launch {
            val result =
                addAlarmUseCase(requestCode, hour, minute, medicationId.intValue)
            if (!result)
                _notificationMessage.value = "Ocurrio un error" else getAlarms(medicationId.intValue)
        }
    }

    private fun getAlarms(medicationId: Int) {
        viewModelScope.launch {
            val result = getAlarmsUseCase(medicationId)
            _alarms.value = result.alarms
            _medicationName.value = result.medication.name
        }
    }

    fun deleteAlarm(alarm: Alarm){
        viewModelScope.launch {
            deleteAlarmUseCase(alarm)
            getAlarms(alarm.ownerId)
        }
    }
}