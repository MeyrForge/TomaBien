package com.meyrforge.tomabien.weekly_summary.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.ui.sharedComponents.ScreenTitleComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink

@Composable
fun WeeklySummaryScreen(viewModel: WeeklySummaryViewModel = hiltViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val trackerWithMedList by viewModel.trackerWithMedicationList.observeAsState()

    val groupedByDate = trackerWithMedList?.groupBy {
        it.tracker.date
    }?.toSortedMap()


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
                ScreenTitleComponent("Resumen semanal")
            }
            groupedByDate?.forEach { (date, trackersForDate) ->
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleLarge,
                        color = PowderedPink,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(trackersForDate.size) { index ->
                    val tracker = trackersForDate[index]
                     Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                        Text("Medicación: ${tracker.medication.name}")
                        Text("Horario: ${tracker.tracker.hour}")
                        Text(text =
                            if (tracker.tracker.taken) "Tomada" else "No tomada"
                        )
                    }
                }
            }
        }
    }
}