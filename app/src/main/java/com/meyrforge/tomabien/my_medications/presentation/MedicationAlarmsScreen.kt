package com.meyrforge.tomabien.my_medications.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.components.AddAlarmDialog
import com.meyrforge.tomabien.my_medications.presentation.components.AlarmListItemComponent
import com.meyrforge.tomabien.ui.sharedComponents.ScreenTitleComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MedicationAlarmsScreen(
    viewModel: MedicationViewModel = hiltViewModel(),
    onSetAlarm: (hour: Int, minute: Int, requestCode: Int, medName: String) -> Unit,
    launchPermission: () -> Unit,
    onCancelAlarm: (Int, String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var openAlertDialog by remember { mutableStateOf(false) }
    val alarms by viewModel.alarms.observeAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { openAlertDialog = true },
                containerColor = PowderedPink,
                contentColor = DeepPurple,
                icon = { Icon(Icons.Outlined.Add, "Agregar alarma") },
                text = {
                    Text("Agregar alarma")
                })
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(DeepPurple)
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            item {
                ScreenTitleComponent(viewModel.medicationName.value)
            }
            if (!alarms.isNullOrEmpty()) {
                for (alarm in alarms!!) {
                    item {
                        AlarmListItemComponent(alarm = alarm, onCancelAlarm = onCancelAlarm)
                    }
                }
            } else {
                item {
                    Text("No hay alarmas")
                }
            }
        }
    }
    if (openAlertDialog) {
        AddAlarmDialog(
            onDismiss = { openAlertDialog = false },
            addAlarm = onSetAlarm,
            launchPermission = launchPermission
        )
    }
}