package com.meyrforge.tomabien.my_medications.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.MedicationViewModel
import com.meyrforge.tomabien.ui.sharedComponents.SegmentedButtonComponent
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
import com.meyrforge.tomabien.ui.theme.pink

@Composable
fun EditMedicationDialog(
    viewModel: MedicationViewModel = hiltViewModel(),
    medToEdit: Medication?,
    onDismiss: () -> Unit
) {
    var med = medToEdit
    if (med != null) {
        viewModel.onMedicationNameChange(med.name)
        viewModel.onMedicationDosageChange(med.dosage)
        viewModel.onIsOptionalChange(med.optional)
    }
    var emptyFields by remember { mutableStateOf(false) }
    AlertDialog(
        icon = {
            Icon(Icons.Outlined.AddCircleOutline, contentDescription = "Icono")
        },
        title = {
            Text(text = if (med == null) "Agregar medicación" else "Editar medicación")
        },
        text = {
            Column {
                NewMedicationContent(med = med)
                if (emptyFields)
                    Text("Completar los campos", color = pink)
            }
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (viewModel.medicationName.value.isEmpty() ||
                        viewModel.medicationDosage.value.isEmpty()
                    ) {
                        emptyFields = true
                    } else {

                        if (med == null) {
                            viewModel.saveMedication()
                        } else {
                            viewModel.onMedicationIdChange(med?.id ?: 0)
                            viewModel.editMedication()
                            med = null
                        }
                    }
                }
            ) {
                Text(if (med == null) "Agregar" else "Editar", color = PowderedPink)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancelar", color = PowderedPink)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        containerColor = DeepPurple,
        iconContentColor = SoftBlueLavander,
        titleContentColor = PowderedPink,
        textContentColor = PowderedPink,
        tonalElevation = 10.dp
    )
}

@Composable
fun NewMedicationContent(viewModel: MedicationViewModel = hiltViewModel(), med: Medication?) {
    val medicationName by viewModel.medicationName
    val medicationDosage by viewModel.medicationDosage
    val isOptional by viewModel.isOptional
    val pattern = remember { Regex("^[0-9,.]+\$") }

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(7f)
                    .padding(2.dp)
            ) {
                OutlinedTextField(
                    value = medicationName,
                    onValueChange = viewModel::onMedicationNameChange,
                    label = { Text("Nombre") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = PowderedPink,
                        unfocusedBorderColor = PowderedPink,
                        unfocusedLabelColor = PowderedPink,
                        unfocusedLeadingIconColor = PowderedPink,
                        focusedBorderColor = SoftBlueLavander,
                        focusedLabelColor = SoftBlueLavander,
                        focusedTextColor = SoftBlueLavander
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Box(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 2.dp)
            ) {
                OutlinedTextField(
                    value = medicationDosage,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(pattern)) viewModel.onMedicationDosageChange(
                            it
                        )
                    },
                    label = { Text("Dosis") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = PowderedPink,
                        unfocusedBorderColor = PowderedPink,
                        unfocusedLabelColor = PowderedPink,
                        unfocusedLeadingIconColor = PowderedPink,
                        focusedBorderColor = SoftBlueLavander,
                        focusedLabelColor = SoftBlueLavander,
                        focusedTextColor = SoftBlueLavander
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
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
            SegmentedButtonComponent(Modifier, isOptional, viewModel::onIsOptionalChange)
        }
        HorizontalDivider(color = SoftBlueLavander, thickness = 3.dp)
        if (med != null) {
            TextButton({ viewModel.deleteMedication(med) }, contentPadding = PaddingValues(0.dp)) {
                Text(
                    "Eliminar medicación",
                    color = pink,
                    fontSize = 16.sp
                )
            }
        }
    }
}

