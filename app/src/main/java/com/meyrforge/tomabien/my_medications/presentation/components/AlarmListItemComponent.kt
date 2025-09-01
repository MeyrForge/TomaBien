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
    onCancelAlarm: (requestCode: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("${alarm.hour}:${alarm.minute}", color = PowderedPink, fontSize = 32.sp)
            Icon(
                Icons.Outlined.Delete,
                "Eliminar alarma",
                tint = pink,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onCancelAlarm(alarm.requestCode)
                        viewModel.deleteAlarm(alarm)
                    }
            )
        }
        HorizontalDivider(thickness = 2.dp, color = PowderedPink)
    }
}