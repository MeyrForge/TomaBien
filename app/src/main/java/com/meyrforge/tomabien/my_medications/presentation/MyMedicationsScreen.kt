package com.meyrforge.tomabien.my_medications.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.components.EditMedicationDialog
import com.meyrforge.tomabien.ui.sharedComponents.ScreenTitleComponent
import com.meyrforge.tomabien.my_medications.presentation.components.SingleMedicationComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.NavBarColor
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MyMedicationsScreen(navController: NavController, viewModel: MedicationViewModel = hiltViewModel()) {
    var medicationToEdit by remember { mutableStateOf<Medication?>(null) }
    val medicationList: List<Medication> by viewModel.medicationList.observeAsState(emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    var openAlertDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {openAlertDialog = true},
                containerColor = PowderedPink,
                contentColor = DeepPurple,
                icon = { Icon(Icons.Outlined.Add, "Agregar") },
                text = {
                    Text("Agregar medicación")
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
                ScreenTitleComponent("Mis Medicaciones")
            }
            if (medicationList.isNotEmpty()) {
                for (med in medicationList) {
                    item {
                        SingleMedicationComponent(
                            navController = navController,
                            med = med,
                            onEdit = {
                                medicationToEdit = med
                                openAlertDialog = true
                            })
                    }
                }
            } else {
                item {
                    Text("No hay medicaciones")
                }
            }

            item {
                Column {
                    Row(modifier = Modifier.padding(horizontal = 5.dp).padding(top = 30.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Album,
                            "Opcional",
                            tint = SoftBlueLavander,
                            modifier = Modifier.weight(1f).size(16.dp)
                        )
                        Text(
                            "Medicación opcional",
                            fontSize = 16.sp,
                            color = PowderedPink,
                            modifier = Modifier.weight(6f)
                        )
                    }

                    Row(modifier = Modifier.padding(horizontal = 5.dp).padding(top = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Album,
                            "No Opcional",
                            tint = NavBarColor,
                            modifier = Modifier.weight(1f).size(16.dp)
                        )
                        Text(
                            "Medicación no opcional",
                            fontSize = 16.sp,
                            color = PowderedPink,
                            modifier = Modifier.weight(6f)
                        )
                    }
                }
            }
        }
        if (openAlertDialog) {
            EditMedicationDialog(medToEdit = medicationToEdit) { openAlertDialog = false }
        }
        LaunchedEffect(medicationList) {
            openAlertDialog = false
            medicationToEdit = null
        }
        NotificationSnackbar(viewModel, snackbarHostState)
    }
}

@Composable
fun NotificationSnackbar(viewModel: MedicationViewModel, snackbarHostState: SnackbarHostState) {
    val notificationMessage by viewModel.notificationMessage.observeAsState()

    LaunchedEffect(notificationMessage) {
        notificationMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }
}