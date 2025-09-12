package com.meyrforge.tomabien.weekly_summary.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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

@Composable
fun WeeklySummaryScreen(viewModel: WeeklySummaryViewModel = hiltViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val trackerList by viewModel.medicationTrackerList.observeAsState()

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
            for (tracker in trackerList ?: emptyList()) {
                item { Text(tracker.toString()) }
            }
        }
    }
}