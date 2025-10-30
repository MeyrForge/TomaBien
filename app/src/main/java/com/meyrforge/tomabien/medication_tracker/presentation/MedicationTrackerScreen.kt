package com.meyrforge.tomabien.medication_tracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.tomabien.R.string
import com.meyrforge.tomabien.common.formatHour
import com.meyrforge.tomabien.medication_tracker.presentation.components.MedicationTrackerItemComponent
import com.meyrforge.tomabien.ui.sharedComponents.ButtonComponent
import com.meyrforge.tomabien.ui.sharedComponents.DateComponent
import com.meyrforge.tomabien.ui.sharedComponents.ScreenTitleComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.pink

@Composable
fun MedicationTrackerScreen(
    viewModel: MedicationTrackerViewModel = hiltViewModel(),
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val list by viewModel.medicationList.observeAsState()

    LaunchedEffect(key1 = true) {
        viewModel.getMedicationWithAlarms()
    }

    Scaffold(
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
                ScreenTitleComponent(stringResource(string.seguimiento))
            }
            item {
                DateComponent()
            }
            list?.let {
                if (it.isNotEmpty()) {
                    for (medWithAlarms in it) {
                        for (alarm in medWithAlarms.alarms) {
                            item {
                                MedicationTrackerItemComponent(
                                    medWithAlarms.medication?.id ?: 0,
                                    formatHour(alarm.hour, alarm.minute),
                                    "${medWithAlarms.medication?.name} ${medWithAlarms.medication?.grammage}"
                                )
                            }
                        }
                    }
                } else {
                    item {
                        Text(stringResource(string.no_hay_medicaciones_con_alarma), color = pink)
                    }
                }
            }
            item {
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    ButtonComponent("Guardar") {
                        viewModel.saveTrackers()
                    }
                }
            }
        }
    }
}