package com.meyrforge.tomabien.weekly_summary.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.tomabien.ui.sharedComponents.ScreenTitleComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.weekly_summary.presentation.components.SummaryItemComponent
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.text.contains

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklySummaryScreen(
    viewModel: WeeklySummaryViewModel = hiltViewModel(),
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val trackerWithMedList by viewModel.trackerWithMedicationList.observeAsState()

    var searchQuery by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val dateFormatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy",
        Locale.getDefault()
    )
    val todayDateString = LocalDate.now().format(dateFormatter)
    var expandedDate by remember { mutableStateOf<String?>(todayDateString) }

    LaunchedEffect(key1 = true) {
        viewModel.getAllTrackersWithMedications()
    }

    val groupedByDate = trackerWithMedList?.groupBy {
        try {
            LocalDate.parse(it.tracker.date, dateFormatter)
        } catch (e: Exception) {
            LocalDate.MIN
        }
    }
        ?.toSortedMap(compareByDescending { it })
        ?.mapKeys {
            it.key.format(dateFormatter)
        }
        ?.filter { (date, _) ->
            date.contains(searchQuery, ignoreCase = true)
        }


    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    // Convertimos los milisegundos seleccionados a formato dd/MM/yyyy
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        searchQuery = date.format(dateFormatter)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepPurple)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                item {
                    ScreenTitleComponent("Resumen de Toma")
                }

                item {
                    val datePattern = remember { Regex("[0-9/]*") }
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { newValue ->
                            if (newValue.matches(datePattern)) {
                                if (newValue.length <= 10) {
                                    searchQuery = newValue
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        placeholder = { Text("Buscar por fecha (dd/mm/aaaa)", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White) },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Filled.CalendarMonth,
                                    contentDescription = "Abrir Calendario",
                                    tint = Color.White
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White
                        )
                    )
                }

                if (groupedByDate.isNullOrEmpty() && searchQuery.isNotEmpty()) {
                    item {
                        Text(
                            "No hay resultados para '$searchQuery'",
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 20.dp)
                        )
                    }
                }


                groupedByDate?.forEach { (date, trackersForDate) ->
                    item {
                        val isExpanded = expandedDate == date
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedDate = if (isExpanded) null else date
                                }
                                .padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (date == todayDateString) "Hoy ($date)" else date,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontSize = 24.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                contentDescription = if (isExpanded) "Cerrar" else "Expandir",
                                tint = Color.White,
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    }
                    items(trackersForDate) { tracker ->
                        AnimatedVisibility(
                            visible = expandedDate == date,
                            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                        ) {
                            Column(modifier = Modifier.padding(2.dp)) {
                                SummaryItemComponent(tracker)
                            }
                        }
                    }
                }
            }
        }
    }
}