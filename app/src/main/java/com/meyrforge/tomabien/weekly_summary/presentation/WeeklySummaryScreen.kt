package com.meyrforge.tomabien.weekly_summary.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.tomabien.R
import com.meyrforge.tomabien.ui.sharedComponents.ScreenTitleComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.weekly_summary.presentation.components.SummaryItemComponent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WeeklySummaryScreen(
    viewModel: WeeklySummaryViewModel = hiltViewModel(),
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val trackerWithMedList by viewModel.trackerWithMedicationList.observeAsState()

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
            Image(
                painter = painterResource(id = R.drawable.new_tb_icon),
                contentDescription = "TomaBien logo de fondo",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f),
                contentScale = ContentScale.Fit
            )
            LazyColumn(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                item {
                    ScreenTitleComponent("Resumen de Toma")
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
                                color = PowderedPink,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = if (isExpanded) "Cerrar" else "Expandir",
                                tint = PowderedPink
                            )
                        }
                    }
                    items(trackersForDate) { tracker ->
                        AnimatedVisibility(
                            visible = expandedDate == date,
                            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                        ) {
                            Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                                SummaryItemComponent(tracker)
                            }
                        }
                    }
                }
            }
        }
    }
}