package com.meyrforge.tomabien.my_medications.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.common.DialogState
import com.meyrforge.tomabien.common.TestTags
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.MedicationViewModel
import com.meyrforge.tomabien.ui.sharedComponents.SegmentedButtonComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.gray
import com.meyrforge.tomabien.ui.theme.lightGray
import com.meyrforge.tomabien.ui.theme.petroleum
import com.meyrforge.tomabien.ui.theme.pink

@Composable
fun EditMedicationDialog(
    viewModel: MedicationViewModel = hiltViewModel(),
    medToEdit: Medication?,
    onCancelAlarm: (Int, String) -> Unit,
    onDismiss: () -> Unit,
) {
    var med = medToEdit
    if (med != null) {
        viewModel.onMedicationNameChange(med.name)
        viewModel.onMedicationGrammageChange(med.grammage)
        viewModel.onIsOptionalChange(med.optional)
        if (!med.countActivated) {
            viewModel.onCountActivatedChange(false)
        } else {
            viewModel.onCountActivatedChange(true)
            viewModel.saveNumberOfPills(med.numberOfPills)
        }
    }
    var emptyFields by remember { mutableStateOf(false) }
    val countActivated by viewModel.countActivated

    AlertDialog(
        icon = {
            Icon(Icons.Outlined.AddCircleOutline, contentDescription = "Icono")
        },
        title = {
            Text(text = if (med == null) "Agregar medicación" else "Editar medicación")
        },
        text = {
            Column {
                NewMedicationContent(med = med, onCancelAlarm = onCancelAlarm)
                if (emptyFields)
                    Text("Completar los campos", color = pink)
            }
        },
        onDismissRequest = {
            onDismiss()
            viewModel.resetValues()
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.testTag(TestTags.ADD_NEW_MEDICATION),
                onClick = {
                    if (viewModel.medicationName.value.isEmpty() ||
                        viewModel.medicationGrammage.value.isEmpty()
                    ) {
                        emptyFields = true
                    } else {

                        if (med == null) {
                            viewModel.saveMedication()
                            if (countActivated) {
                                viewModel.onIsAddPillVisibleChange(DialogState.CHANGE_PILLS)
                            } else {
                                onDismiss()
                            }
                        } else {
                            viewModel.onMedicationIdChange(med?.id ?: 0)
                            viewModel.editMedication()
                            if (countActivated && med?.countActivated == false) {
                                viewModel.onMedicationToCountChange(
                                    med!!.copy(
                                        countActivated = true,
                                        numberOfPills = 0f
                                    )
                                )
                                med = null
                                viewModel.onIsAddPillVisibleChange(DialogState.CHANGE_PILLS)
                            } else {
                                onDismiss()
                                med = null
                            }
                        }
                    }
                }
            ) {
                Text(if (med == null) "Agregar" else "Editar", color = petroleum)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    viewModel.resetValues()
                }
            ) {
                Text("Cancelar", color = Color.White)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        containerColor = DeepPurple,
        iconContentColor = petroleum,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        tonalElevation = 10.dp
    )
}

@Composable
fun NewMedicationContent(
    viewModel: MedicationViewModel = hiltViewModel(),
    med: Medication?,
    onCancelAlarm: (Int, String) -> Unit,
) {
    val notificationMessage by viewModel.notificationMessage.observeAsState()
    val medicationName by viewModel.medicationName
    val medicationGrammage by viewModel.medicationGrammage
    val isOptional by viewModel.isOptional
    val countActivated by viewModel.countActivated
    val pattern = remember { Regex("^[0-9,.]+\$") }
    var isMiligrams by remember { mutableStateOf(true) }

    Column() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            TextField(
                value = medicationName,
                onValueChange = viewModel::onMedicationNameChange,
                label = { Text("Nombre") },
                colors = TextFieldDefaults.colors(
                    unfocusedLabelColor = lightGray,
                    unfocusedIndicatorColor = lightGray,
                    focusedIndicatorColor = petroleum,
                    focusedLabelColor = petroleum,
                    focusedTextColor = lightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.NEW_MEDICATION_NAME)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(vertical = 6.dp)
        ) {
            TextField(
                value = medicationGrammage,
                onValueChange = {
                    if (it.isEmpty() || it.matches(pattern)) viewModel.onMedicationGrammageChange(
                        it
                    )
                },
                label = { Text("Gramaje") },
                colors = TextFieldDefaults.colors(
                    unfocusedLabelColor = lightGray,
                    unfocusedIndicatorColor = lightGray,
                    focusedIndicatorColor = petroleum,
                    focusedLabelColor = petroleum,
                    focusedTextColor = lightGray
                ),
                modifier = Modifier
                    .weight(6f)
                    .testTag(TestTags.NEW_MEDICATION_GRAMMAGE)
            )
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .padding(start = 6.dp)
                    .clip(RoundedCornerShape(5.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .background(if (isMiligrams) petroleum else gray)
                        .width(35.dp)
                        .height(50.dp)
                        .padding(4.dp)
                        .clickable{isMiligrams = !isMiligrams}
                ) {
                    Text(
                        "mg",
                        fontSize = 18.sp,
                        color = Color.White,
                        )
                }
                VerticalDivider()
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .background(if (!isMiligrams) petroleum else gray)
                        .width(35.dp)
                        .height(50.dp)
                        .padding(4.dp)
                        .clickable{isMiligrams = !isMiligrams}
                ) {
                    Text(
                        "gr",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            Text("Opcional", fontSize = 18.sp)
            SegmentedButtonComponent(
                Modifier.testTag(TestTags.OPTIONAL_CHECK),
                isOptional,
                viewModel::onIsOptionalChange
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            Text("Activar conteo de pastillas", fontSize = 18.sp, modifier = Modifier.weight(5f))
            SegmentedButtonComponent(
                Modifier.testTag(TestTags.ACTIVATE_PILL_CHECK),
                countActivated,
                viewModel::onCountActivatedChange
            )
        }

        if (countActivated) {
            Text(
                "Vas a poder editar la cantidad de pastillas que tengas apretando el ícono de blister",
                color = petroleum,
                modifier = Modifier.padding(4.dp)
            )
        }

        HorizontalDivider(
            color = gray,
            thickness = 3.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
        notificationMessage?.let {
            Text(it, color = pink)
        }
        if (med != null) {
            TextButton({
                viewModel.deleteMedication(med)
                viewModel.getAlarms(med.id ?: 0)
                viewModel.alarms.value.let {
                    it?.forEach { alarm -> onCancelAlarm(alarm.requestCode, med.name) }
                }
            }, contentPadding = PaddingValues(0.dp)) {
                Text(
                    "Eliminar medicación",
                    color = pink,
                    fontSize = 16.sp
                )
            }
        }
    }
}

