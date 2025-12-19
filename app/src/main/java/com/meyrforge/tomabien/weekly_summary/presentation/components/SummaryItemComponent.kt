package com.meyrforge.tomabien.weekly_summary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.outlined.AccessAlarm
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.meyrforge.tomabien.R
import com.meyrforge.tomabien.ui.theme.NavBarColor
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.gray
import com.meyrforge.tomabien.ui.theme.green
import com.meyrforge.tomabien.ui.theme.lightGray
import com.meyrforge.tomabien.ui.theme.petroleum
import com.meyrforge.tomabien.ui.theme.pink
import com.meyrforge.tomabien.ui.theme.purple
import com.meyrforge.tomabien.weekly_summary.domain.models.TrackerWithMedicationData

@Composable
fun SummaryItemComponentOld(tracker: TrackerWithMedicationData) {
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
                    PowderedPink.copy(alpha = 0.8f),
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
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(4.dp)) {
                        Icon(Icons.Outlined.AccessAlarm, "Hora", tint = Color.White)
                        Text(tracker.tracker.hour, fontSize = 16.sp, color = Color.White)
                    }
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(4.dp)) {
                        Icon(
                            painterResource(R.drawable.ic_blister_xml),
                            "Conteo",
                            modifier = Modifier.size(24.dp),
                            tint = if (!tracker.medication.countActivated) NavBarColor else Color.White
                        )
                        if (!tracker.medication.countActivated) {
                            Text("Conteo desactivado", fontSize = 16.sp, color = Color.White)
                        } else {
                            val numberOfPills = tracker.medication.numberOfPills
                            val count =
                                if (numberOfPills % 1.0f == 0f) numberOfPills.toInt() else numberOfPills
                            Text(
                                if (count == 1) "Queda 1 pastilla" else "Quedan $count pastillas",
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
                        tint = Color.White,
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

@Composable
fun SummaryItemComponent(tracker: TrackerWithMedicationData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(gray.copy(alpha = 0.9f), RoundedCornerShape(30.dp))
            .padding(6.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background((if (tracker.medication.optional) petroleum else purple).copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(R.drawable.ic_pills),
                    contentDescription = "Medicacion",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(
                tracker.tracker.hour,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 6.dp)
            )
        }
            Column {
                Text(
                    "${tracker.medication.name} ${tracker.medication.grammage}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
                Box(modifier = Modifier.padding(4.dp)) {
                    if (tracker.tracker.taken) {
                        PersonalizedChipComponent(green, "Tomada")
                    } else {
                        PersonalizedChipComponent(pink, "No tomada", Icons.Filled.RemoveCircle)
                    }
                }

                Box(modifier = Modifier.padding(4.dp)) {
                    if (!tracker.medication.countActivated) {
                        PersonalizedChipComponent(
                            petroleum,
                            "Conteo desactivado",
                            Icons.Filled.Medication
                        )
                    } else {
                        val numberOfPills = tracker.medication.numberOfPills
                        val count =
                            if (numberOfPills % 1.0f == 0f) numberOfPills.toInt() else numberOfPills
                        PersonalizedChipComponent(
                            purple,
                            if (count == 1) "Queda 1 pastilla" else "Quedan $count pastillas",
                            Icons.Filled.Medication
                        )
                    }
                }
            }
//        Row(modifier = Modifier.padding(12.dp)) {
//        }
    }
}