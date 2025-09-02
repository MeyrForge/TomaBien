package com.meyrforge.tomabien.medication_tracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meyrforge.tomabien.ui.sharedComponents.SegmentedButtonComponent
import com.meyrforge.tomabien.ui.theme.PowderedPink

@Composable
fun MedicationTrackerItemComponent(hour: String, medName: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                hour,
                fontSize = 24.sp,
                color = PowderedPink
            )
            Text(
                medName,
                fontSize = 24.sp,
                color = PowderedPink
            )
            SegmentedButtonComponent(Modifier, true) { }
        }
        HorizontalDivider(thickness = 2.dp, color = PowderedPink)
    }
}