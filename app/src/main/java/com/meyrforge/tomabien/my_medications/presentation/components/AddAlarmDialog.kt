package com.meyrforge.tomabien.my_medications.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.common.TestTags
import com.meyrforge.tomabien.my_medications.presentation.MedicationViewModel
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
import com.meyrforge.tomabien.ui.theme.pink
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlarmDialog(
    viewModel: MedicationViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    addAlarm: (hour: Int, minute: Int, requestCode: Int, medName: String) -> Unit,
    launchPermission: () -> Unit
) {
    val notificationMessage = viewModel.notificationMessage.observeAsState()
    val currentTime = Calendar.getInstance()
    launchPermission()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )
    val code =
        "${timePickerState.hour}${timePickerState.minute}${viewModel.medicationId.intValue}".toInt()
    AlertDialog(
        icon = { Icon(Icons.Outlined.Alarm, "Alarma") },
        title = { Text("Agregar alarma") },
        text = {
            Column {
                AddAlarmDialogContent(timePickerState)
                notificationMessage.value?.let {
                    Text(it, color = pink)
                }
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                modifier = Modifier.testTag(TestTags.ADD_ALARM),
                onClick = {
                    viewModel.addAlarm(
                        code,
                        timePickerState.hour,
                        timePickerState.minute
                    )
                    if (notificationMessage.value.isNullOrEmpty()) {
                        addAlarm(
                            timePickerState.hour,
                            timePickerState.minute,
                            code,
                            viewModel.medicationName.value
                        )
                        onDismiss()
                    }

                }
            ) {
                Text("Agregar", color = PowderedPink)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancelar", color = PowderedPink)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        containerColor = DeepPurple,
        iconContentColor = SoftBlueLavander,
        titleContentColor = PowderedPink,
        textContentColor = PowderedPink,
        tonalElevation = 10.dp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlarmDialogContent(
    picker: TimePickerState,
    viewModel: MedicationViewModel = hiltViewModel()
) {
    val medicationDosage by viewModel.medicationDosage
    val patternFloat = remember { Regex("^[0-9.]+\$") }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        TimeInput(
            state = picker,
            colors = TimePickerDefaults.colors(
                selectorColor = PowderedPink,
                timeSelectorSelectedContainerColor = PowderedPink,
                timeSelectorSelectedContentColor = DeepPurple
            )
        )
        OutlinedTextField(
            value = medicationDosage,
            onValueChange = {
                if (it.isEmpty() || it.matches(patternFloat)) viewModel.onMedicationDosageChange(
                    it
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Dosis") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = PowderedPink,
                unfocusedBorderColor = PowderedPink,
                unfocusedLabelColor = PowderedPink,
                unfocusedLeadingIconColor = PowderedPink,
                focusedBorderColor = SoftBlueLavander,
                focusedLabelColor = SoftBlueLavander,
                focusedTextColor = SoftBlueLavander
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}