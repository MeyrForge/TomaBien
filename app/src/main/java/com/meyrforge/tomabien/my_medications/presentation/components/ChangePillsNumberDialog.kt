package com.meyrforge.tomabien.my_medications.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.R
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.MedicationViewModel
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander

@Composable
fun ChangePillsNumberDialog( medication: Medication, onDismiss: () -> Unit) {
    AlertDialog(
        icon = {
            Icon(
                painterResource(R.drawable.ic_blister_xml),
                "Blister",
                modifier = Modifier.size(32.dp)
            )
        },
        title = { Text("${medication.name} ${medication.grammage}") },
        text = { PillDialogContent(medication.numberOfPills.toString()) },
        onDismissRequest = {onDismiss()},
        confirmButton = {
            TextButton({}) {
                Text("Guardar", color = PowderedPink)
            }
        },
        dismissButton = {
            TextButton({onDismiss()}) {
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
fun PillDialogContent(numberOfPills: String = "0", viewModel: MedicationViewModel = hiltViewModel()) {
    val pattern = remember { Regex("^[0-9,.]+\$") }
    val numberOfPills by viewModel.numberOfPills

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(numberOfPills, fontSize = 40.sp, color = SoftBlueLavander)
        Text("Pastillas", color = PowderedPink)
        HorizontalDivider(color = SoftBlueLavander, thickness = 3.dp, modifier = Modifier.padding(vertical = 20.dp))
        TextField(
            value = numberOfPills,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(pattern)) {
                    viewModel.onNumberOfPillsChange(newValue)
                }
            },
            label = { Text("Nueva cantidad") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedTextColor = SoftBlueLavander,
                unfocusedTextColor = SoftBlueLavander,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = SoftBlueLavander,
                unfocusedIndicatorColor = PowderedPink.copy(alpha = 0.7f),
                focusedLabelColor = PowderedPink,
                unfocusedLabelColor = PowderedPink.copy(alpha = 0.7f),
                cursorColor = SoftBlueLavander
            )
        )
    }
}