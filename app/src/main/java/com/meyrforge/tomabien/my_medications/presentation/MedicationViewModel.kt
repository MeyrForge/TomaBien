package com.meyrforge.tomabien.my_medications.presentation

import androidx.compose.runtime.mutableFloatStateOf
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
import com.meyrforge.tomabien.my_medications.domain.usecases.UpdateNumberOfPillsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveMedicationUseCase: SaveMedicationUseCase,
    private val getAllMedicationsUseCase: GetAllMedicationsUseCase,
    private val editMedicationUseCase: EditMedicationUseCase,
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val addAlarmUseCase: AddAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val updateNumberOfPillsUseCase: UpdateNumberOfPillsUseCase,
) :
    ViewModel() {
    private val _medicationId = mutableIntStateOf(0)
    val medicationId = _medicationId

    private val _medicationName = mutableStateOf("")
    val medicationName = _medicationName

    private val _medicationDosage = mutableStateOf("")
    val medicationDosage = _medicationDosage

    private val _numberOfPills = mutableFloatStateOf(0f)
    val numberOfPills = _numberOfPills

    private val _medicationGrammage = mutableStateOf("")
    val medicationGrammage = _medicationGrammage

    private val _isOptional = mutableStateOf(false)
    val isOptional = _isOptional

    private val _countActivated = mutableStateOf(false)
    val countActivated = _countActivated

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

    fun onCountActivatedChange(countActivated: Boolean) {
        _countActivated.value = countActivated
    }

    fun onNumberOfPillsChange(numberOfPills: Float) {
        _numberOfPills.floatValue = numberOfPills
    }

    fun onMedicationGrammageChange(grammage: String) {
        _medicationGrammage.value = grammage
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
        if (_medicationName.value.isEmpty() ||
            _medicationGrammage.value.isEmpty()
        ) {
            _notificationMessage.value = "Completar los campos"
        } else {
            if (_countActivated.value) {
                _numberOfPills.floatValue = 0f
            } else {
                _numberOfPills.floatValue = -1f
            }
            viewModelScope.launch {
                val result = saveMedicationUseCase(
                    _medicationName.value,
                    _medicationGrammage.value,
                    _isOptional.value,
                    _numberOfPills.floatValue,
                    _countActivated.value

                )
                if (result) {
                    resetValues()
                    getAllMedications()
                    _notificationMessage.value = "Medicacion agregada"
                } else {
                    _notificationMessage.value = "Algo salio mal"
                }
            }
        }
    }

    fun getAllMedications() {
        viewModelScope.launch {
            val medList = getAllMedicationsUseCase()

            if (medList != null)
                _medicationList.value = medList.filter { !it.deleted }
        }
    }

    fun editMedication() {
        if (_medicationName.value.isEmpty() ||
            _medicationGrammage.value.isEmpty()
        ) {
            _notificationMessage.value = "Completar los campos"
        } else {
            if (_countActivated.value) {
                _numberOfPills.floatValue = 0f
            } else {
                _numberOfPills.floatValue = -1f
            }
            viewModelScope.launch {
                val result = editMedicationUseCase(
                    Medication(
                        medicationId.intValue,
                        medicationName.value,
                        _medicationGrammage.value,
                        isOptional.value,
                        numberOfPills.floatValue,
                        _countActivated.value
                    )
                )
                if (result) {
                    resetValues()
                    getAllMedications()
                    _notificationMessage.value = "Medicacion editada"
                } else {
                    _notificationMessage.value = "Algo salio mal"
                }
            }
        }
    }

    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            medication.deleted = true
            val result = editMedicationUseCase(medication)
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
        if (_medicationDosage.value.isEmpty()){
            _notificationMessage.value = "Agregar dosis"
        }else{
            clearMessage()
            viewModelScope.launch {
                val result =
                    addAlarmUseCase(requestCode, hour, minute, medicationId.intValue, _medicationDosage.value.toFloat())
                if (!result)
                    _notificationMessage.value =
                        "Ocurrio un error" else getAlarms(medicationId.intValue)
            }
        }
    }

    private fun getAlarms(medicationId: Int) {
        viewModelScope.launch {
            val result = getAlarmsUseCase(medicationId)
            result?.let {
                _alarms.value = it.alarms
                _medicationName.value = it.medication?.name ?: "Sin nombre"

            }
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            deleteAlarmUseCase(alarm)
            getAlarms(alarm.ownerId)
        }
    }

    fun saveNumberOfPills(numberOfPills: Float) {
        _numberOfPills.floatValue = numberOfPills
    }

    fun resetValues() {
        _medicationName.value = ""
        _medicationGrammage.value = ""
        _isOptional.value = false
        _countActivated.value = false
    }

    fun updateNumberOfPills(medicationId: Int, numberOfPills: Float) {
        viewModelScope.launch {
            val result = updateNumberOfPillsUseCase(medicationId, numberOfPills)
            if (!result) {
                _notificationMessage.value = "Ocurrió un error"
            } else {
                _numberOfPills.floatValue = numberOfPills
                getAllMedications()
                _notificationMessage.value = "Número de pastillas actualizado"
            }
        }
    }
}