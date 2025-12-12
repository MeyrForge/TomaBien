package com.meyrforge.tomabien.my_medications.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.components.ChangePillsNumberDialog
import com.meyrforge.tomabien.my_medications.presentation.components.EditMedicationDialog
import com.meyrforge.tomabien.my_medications.presentation.components.IconExplainedComponent
import com.meyrforge.tomabien.my_medications.presentation.components.SingleMedicationComponent
import com.meyrforge.tomabien.ui.sharedComponents.ScreenTitleComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.LightWarmGray
import com.meyrforge.tomabien.ui.theme.NavBarColor
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander

@Composable
fun MyMedicationsScreen(
    navController: NavController,
    onCancelAlarm: (Int, String) -> Unit,
    viewModel: MedicationViewModel = hiltViewModel(),
) {
    var medicationToEdit by remember { mutableStateOf<Medication?>(null) }
    var medicationToNumber by remember { mutableStateOf<Medication?>(null) }
    val medicationList: List<Medication> by viewModel.medicationList.observeAsState(emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    var openAlertDialog by remember { mutableStateOf(false) }
    var openAlertDialogNew by remember { mutableStateOf(false) }
    var openPillsDialog by remember { mutableStateOf(false) }

    var isAddPillVisible by viewModel.isAddPillVisible

    LaunchedEffect(key1 = true) {
        viewModel.getAllMedications()
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { openAlertDialogNew = true },
                containerColor = PowderedPink,
                contentColor = DeepPurple,
                icon = { Icon(Icons.Outlined.Add, "Agregar medicación") },
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
                            onPillsOpened = {
                                medicationToNumber = med
                                openPillsDialog = true
                            },
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
                Spacer(modifier = Modifier.size(24.dp))
                Column {
                    Text(
                        "Glosario",
                        fontSize = 18.sp,
                        color = SoftBlueLavander,
                        fontWeight = FontWeight.Bold
                    )
                    IconExplainedComponent(
                        Icons.Outlined.Album,
                        "Medicación opcional",
                        SoftBlueLavander
                    )
                    IconExplainedComponent(
                        Icons.Outlined.Album,
                        "Medicación no opcional",
                        NavBarColor
                    )
                    IconExplainedComponent(
                        Icons.Outlined.Edit, "Editar o eliminar medicación",
                        LightWarmGray
                    )
                    IconExplainedComponent(
                        Icons.Outlined.Alarm, "Agregar o eliminar alarmas",
                        SoftBlueLavander
                    )
                    IconExplainedComponent(
                        isBlister = true,
                        text = "Conteo de pastillas",
                        tint = PowderedPink
                    )
                }
            }
        }
        if (openAlertDialog) {
            AnimatedContent(
                targetState = isAddPillVisible,
                transitionSpec = { fadeIn().togetherWith(fadeOut()) },
                content = { visible ->
                    if (!visible) {
                        EditMedicationDialog(
                            medToEdit = medicationToEdit,
                            onCancelAlarm = onCancelAlarm
                        ) { openAlertDialog = false }
                    } else {
                        medicationToNumber?.let {
                            ChangePillsNumberDialog(medication = it) { openPillsDialog = false }
                        }
                    }
                })
        }
        if (openPillsDialog) {
            medicationToNumber?.let {
                ChangePillsNumberDialog(medication = it) { openPillsDialog = false }
            }
        }
        if (openAlertDialogNew) {
            AnimatedContent(
                targetState = isAddPillVisible,
                transitionSpec = { fadeIn().togetherWith(fadeOut()) },
                content = { visible ->
                    if (!visible) {
                        EditMedicationDialog(
                            medToEdit = null,
                            onCancelAlarm = onCancelAlarm
                        ) { openAlertDialogNew = false }
                    } else {
                        medicationToNumber?.let {
                            ChangePillsNumberDialog(medication = it) { openPillsDialog = false }
                        }
                    }
                })
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