package com.meyrforge.tomabien.my_medications.presentation

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.tomabien.common.DialogState
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.components.ChangePillsNumberDialog
import com.meyrforge.tomabien.my_medications.presentation.components.EditMedicationDialog
import com.meyrforge.tomabien.my_medications.presentation.components.SingleMedicationComponent
import com.meyrforge.tomabien.ui.sharedComponents.ScreenTitleComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.gray

@Composable
fun MyMedicationsScreen(
    navController: NavController,
    onCancelAlarm: (Int, String) -> Unit,
    viewModel: MedicationViewModel = hiltViewModel(),
) {
    var medicationToEdit by remember { mutableStateOf<Medication?>(null) }
    val medicationToNumber by viewModel.medicationToCount.observeAsState()
    val medicationList: List<Medication> by viewModel.medicationList.observeAsState(emptyList())
    val snackbarHostState = remember { SnackbarHostState() }

    val currentDialog by viewModel.isAddPillVisible.observeAsState(DialogState.HIDDEN)

    LaunchedEffect(key1 = true) {
        viewModel.getAllMedications()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //openAlertDialogNew = true
                    viewModel.onIsAddPillVisibleChange(DialogState.ADD_MEDICATION)
                },
                containerColor = gray,
                contentColor = Color.White,
                content = { Icon(Icons.Outlined.Add, "Agregar medicación") },
                )
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
                                //medicationToNumber = med
                                viewModel.onMedicationToCountChange(med)
                                viewModel.onIsAddPillVisibleChange(DialogState.CHANGE_PILLS)
                                //openPillsDialog = true
                            },
                            onEdit = {
                                medicationToEdit = med
                                viewModel.onIsAddPillVisibleChange(DialogState.EDIT_MEDICATION)
                                //openAlertDialog = true
                            })
                    }
                }
            } else {
                item {
                    Text("No hay medicaciones")
                }
            }

            /*item {
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
            }*/
        }
        AnimatedContent(
            targetState = currentDialog,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                if (targetState != DialogState.HIDDEN) {
                    slideInVertically { fullHeight -> fullHeight } + fadeIn() togetherWith
                            slideOutVertically { fullHeight -> -fullHeight } + fadeOut()
                } else {
                    slideInVertically { fullHeight -> -fullHeight } + fadeIn() togetherWith
                            slideOutVertically { fullHeight -> fullHeight } + fadeOut()
                }
            },
            label = "DialogAnimation",
        ) { dialogState ->
            when (dialogState) {
                DialogState.EDIT_MEDICATION -> {
                    EditMedicationDialog(
                        medToEdit = medicationToEdit,
                        onCancelAlarm = onCancelAlarm,
                        onDismiss = {
                            viewModel.onIsAddPillVisibleChange(DialogState.HIDDEN)
                            medicationToEdit = null
                        }
                    )
                }

                DialogState.CHANGE_PILLS -> {
                    medicationToNumber?.let { med ->
                        ChangePillsNumberDialog(
                            medication = med,
                            onDismiss = {
                                viewModel.onIsAddPillVisibleChange(DialogState.HIDDEN)
                                viewModel.resetMedicationToCount()
                            }
                        )
                    }
                }

                DialogState.HIDDEN -> {
                    viewModel.resetValues()
                }

                DialogState.ADD_MEDICATION -> {
                    EditMedicationDialog(
                        medToEdit = null,
                        onCancelAlarm = onCancelAlarm,
                        onDismiss = {
                            viewModel.onIsAddPillVisibleChange(DialogState.HIDDEN)
                        }
                    )
                }
            }
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