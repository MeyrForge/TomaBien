package com.meyrforge.tomabien.my_medications.presentation

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.components.EditMedicationDialog
import com.meyrforge.tomabien.my_medications.presentation.components.ScreenTitleComponent
import com.meyrforge.tomabien.my_medications.presentation.components.SingleMedicationComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink

@Preview
@Composable
fun MyMedicationsScreen(viewModel: MedicationViewModel = hiltViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    var openAlertDialog by remember { mutableStateOf(false) }
    var medicationToEdit by remember { mutableStateOf<Medication?>(null) }
    val medicationList: List<Medication> by viewModel.medicationList.observeAsState(emptyList())

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {openAlertDialog = true},
                containerColor = PowderedPink,
                contentColor = DeepPurple,
                icon = { Icon(Icons.Outlined.Add, "Agregar") },
                text = {
                    Text("Agregar medicacion")
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
        }
        if (openAlertDialog) {
            EditMedicationDialog(med = medicationToEdit) { openAlertDialog = false }
        }
        LaunchedEffect(medicationList) {
            openAlertDialog = false
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