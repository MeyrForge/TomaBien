package com.meyrforge.tomabien.weekly_summary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessAlarm
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meyrforge.tomabien.R
import com.meyrforge.tomabien.ui.theme.NavBarColor
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.weekly_summary.domain.models.TrackerWithMedicationData

@Composable
fun SummaryItemComponent(tracker: TrackerWithMedicationData) {
    Column {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                Icons.Outlined.Medication,
                "Medicación",
                tint = PowderedPink,
                modifier = Modifier.size(24.dp)
            )
            Text(
                "${tracker.medication.name} ${tracker.medication.grammage}",
                fontSize = 18.sp,
                color = Color.White
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .background(
                    PowderedPink.copy(alpha = 0.4f),
                    RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(8f)
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Outlined.AccessAlarm, "Hora", tint = PowderedPink)
                        Text(tracker.tracker.hour, fontSize = 16.sp, color = Color.White)
                    }
                    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(8.dp)) {
                        Icon(
                            painterResource(R.drawable.ic_blister_xml),
                            "Conteo",
                            modifier = Modifier.size(24.dp),
                            tint = if (!tracker.medication.countActivated) NavBarColor else PowderedPink
                        )
                        if (!tracker.medication.countActivated) {
                            Text("Conteo desactivado", fontSize = 14.sp, color = Color.White)
                        } else {
                            val numberOfPills = tracker.medication.numberOfPills
                            val count = if (numberOfPills % 1.0f == 0f) numberOfPills.toInt() else numberOfPills
                            Text(
                                if (count == 1) "1 pastilla" else "$count pastillas",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Tomada", fontSize = 16.sp, color = Color.White)
                if (tracker.tracker.taken) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        "Fue tomada",
                        tint = PowderedPink,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        Icons.Outlined.RemoveCircleOutline,
                        "No fue tomada",
                        tint = NavBarColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}