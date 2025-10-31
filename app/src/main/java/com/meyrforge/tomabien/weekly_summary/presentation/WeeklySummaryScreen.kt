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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.tomabien.ui.sharedComponents.ScreenTitleComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.weekly_summary.presentation.components.SummaryItemComponent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WeeklySummaryScreen(viewModel: WeeklySummaryViewModel = hiltViewModel(), navController : NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val trackerWithMedList by viewModel.trackerWithMedicationList.observeAsState()

    val dateFormatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy",
        Locale.getDefault()
    )

    LaunchedEffect(key1 = true) {
        viewModel.getAllTrackersWithMedications()
    }

    val groupedByDate = trackerWithMedList?.groupBy {
        // Parse the date string to LocalDate for proper sorting
        try {
            LocalDate.parse(it.tracker.date, dateFormatter)
        } catch (e: Exception) {
            // Handle potential parsing errors, e.g., log or use a default date
            // For simplicity, this example uses a very old date if parsing fails,
            // which would put unparseable dates at the beginning.
            // Adjust this error handling as needed for your application.
            LocalDate.MIN
        }
    }
        ?.toSortedMap(compareByDescending { it }) // Sort by LocalDate in descending order (latest first)
        ?.mapKeys {
            // Convert the LocalDate key back to String for display
            it.key.format(dateFormatter)
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
                ScreenTitleComponent("Resumen de Toma")
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
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        SummaryItemComponent(tracker)
                    }
                }
            }
        }
    }
}