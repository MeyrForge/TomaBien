package com.meyrforge.tomabien.my_medications.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.my_medications.presentation.MedicationViewModel
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
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
    val code = "${timePickerState.hour}${timePickerState.minute}${viewModel.medicationId.intValue}".toInt()
    AlertDialog(
        icon = { Icon(Icons.Outlined.Alarm, "Alarma") },
        title = { Text("Agregar alarma") },
        text = { AddAlarmDialogContent(timePickerState) },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.addAlarm(
                        code,
                        timePickerState.hour,
                        timePickerState.minute,
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
fun AddAlarmDialogContent(picker: TimePickerState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        TimePicker(
            state = picker,
            colors = TimePickerDefaults.colors(
                selectorColor = PowderedPink,
                timeSelectorSelectedContainerColor = PowderedPink,
                timeSelectorSelectedContentColor = DeepPurple
            )
        )
    }
}