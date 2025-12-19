package com.meyrforge.tomabien.medication_tracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.meyrforge.tomabien.ui.theme.petroleum
import com.meyrforge.tomabien.ui.theme.pink

@Composable
fun MedicationTrackerScreen(
    viewModel: MedicationTrackerViewModel = hiltViewModel(),
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val list by viewModel.medicationList.observeAsState()
    val notificationMessage by viewModel.notificationMessage.observeAsState()

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
                Row(modifier = Modifier.padding(top = 24.dp).fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { viewModel.saveTrackers() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = petroleum,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(60.dp).clip(RoundedCornerShape(20.dp))
                    ) {
                        Icon(Icons.Filled.Check, "Guardar", tint = Color.White)
                        Text("Guardar", modifier = Modifier.padding(start = 6.dp))
                    }
                }
            }
        }
        if (notificationMessage != null) {
            LaunchedEffect(notificationMessage) {
                snackbarHostState.showSnackbar(notificationMessage!!)
                viewModel.clearMessage()
            }
        }
    }
}

@Preview
@Composable
fun boton() {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = petroleum, contentColor = Color.White)
    ) {
    }
}