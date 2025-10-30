package com.meyrforge.tomabien.my_medications.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.presentation.MedicationViewModel
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.pink

@Composable
fun AlarmListItemComponent(
    viewModel: MedicationViewModel = hiltViewModel(),
    alarm: Alarm,
    onCancelAlarm: (requestCode: Int, medName: String) -> Unit
) {
    val hour = if (alarm.hour.toString().length == 1){ "0"+alarm.hour.toString()} else alarm.hour.toString()
    val minute = if (alarm.minute.toString().length == 1){ "0"+alarm.minute.toString()} else alarm.minute.toString()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("$hour:$minute - Dosis ${alarm.dosage}", color = PowderedPink, fontSize = 28.sp)
            Icon(
                Icons.Outlined.Delete,
                "Eliminar alarma",
                tint = pink,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        onCancelAlarm(alarm.requestCode, viewModel.medicationName.value)
                        viewModel.deleteAlarm(alarm)
                    }
            )
        }
        HorizontalDivider(thickness = 2.dp, color = PowderedPink)
    }
}