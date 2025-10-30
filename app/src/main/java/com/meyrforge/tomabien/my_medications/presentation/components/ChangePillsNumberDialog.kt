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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.R
import com.meyrforge.tomabien.common.TestTags
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.MedicationViewModel
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander

@Composable
fun ChangePillsNumberDialog( viewModel: MedicationViewModel = hiltViewModel(), medication: Medication, onDismiss: () -> Unit) {
    var currentPillsValue by remember { mutableStateOf(medication.numberOfPills.toString()) }

    AlertDialog(
        icon = {
            Icon(
                painterResource(R.drawable.ic_blister_xml),
                "Blister",
                modifier = Modifier.size(32.dp)
            )
        },
        title = { Text("${medication.name} ${medication.grammage}") },
        text = {
            PillDialogContent(
                initialValue = medication.numberOfPills,
                onValueChange = { newTextValue ->
                    currentPillsValue = newTextValue
                }
            )
        },
        onDismissRequest = {onDismiss()},
        confirmButton = {
            TextButton(onClick = {
                val finalValue = currentPillsValue.toFloatOrNull() ?: 0f
                viewModel.updateNumberOfPills(medication.id ?: 0, finalValue)
                onDismiss()
            },
                modifier = Modifier.testTag(TestTags.SAVE_PILL_AMOUNT)) {
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
fun PillDialogContent(initialValue: Float, onValueChange: (String) -> Unit) {
    val pattern = remember {  Regex("^\\d*\\.?\\d*\$")  }
    var textValue by remember {
        mutableStateOf(
            if (initialValue == initialValue.toInt().toFloat()) {
                initialValue.toInt().toString() // Si es un entero como 12.0, muestra "12"
            } else {
                initialValue.toString() // Si es decimal como 12.5, muestra "12.5"
            }
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(textValue.ifEmpty { "0" }, fontSize = 40.sp, color = SoftBlueLavander, modifier = Modifier.semantics{contentDescription = "Cantidad de pastillas actual"})
        Text(if (textValue == "1") "Pastilla" else "Pastillas", color = PowderedPink)
        HorizontalDivider(color = SoftBlueLavander, thickness = 3.dp, modifier = Modifier.padding(vertical = 20.dp))
        TextField(
            modifier = Modifier.padding(bottom = 10.dp),
            value = textValue,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(pattern)) {
                    textValue = newValue // Actualiza el estado local directamente
                    onValueChange(newValue) // Notifica al composable padre del nuevo valor en formato String
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
        Text("TomaBien te va a notificar cuando te queden 5 o menos pastillas", color = SoftBlueLavander, textAlign = TextAlign.Center)
    }
}