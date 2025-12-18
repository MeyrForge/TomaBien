package com.meyrforge.tomabien.my_medications.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.common.alarm.Alarm
import com.meyrforge.tomabien.my_medications.presentation.MedicationViewModel
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.gray
import com.meyrforge.tomabien.ui.theme.lightGray
import com.meyrforge.tomabien.ui.theme.petroleum
import com.meyrforge.tomabien.ui.theme.pink

@Composable
fun AlarmListItemComponentOld(
    viewModel: MedicationViewModel = hiltViewModel(),
    alarm: Alarm,
    onCancelAlarm: (requestCode: Int, medName: String) -> Unit
) {
    val hour = if (alarm.hour.toString().length == 1) {
        "0" + alarm.hour.toString()
    } else alarm.hour.toString()
    val minute = if (alarm.minute.toString().length == 1) {
        "0" + alarm.minute.toString()
    } else alarm.minute.toString()

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


@Composable
fun AlarmListItemComponent(
    viewModel: MedicationViewModel = hiltViewModel(),
    alarm: Alarm,
    onCancelAlarm: (requestCode: Int, medName: String) -> Unit
) {
    var isOptionsVisible by remember { mutableStateOf(false) }

    val hour = if (alarm.hour.toString().length == 1) {
        "0" + alarm.hour.toString()
    } else alarm.hour.toString()
    val minute = if (alarm.minute.toString().length == 1) {
        "0" + alarm.minute.toString()
    } else alarm.minute.toString()

    val finalDosis = if (alarm.dosage % 1.0 == 0.0) {
        alarm.dosage.toInt().toString()
    }else{
        alarm.dosage.toString()
    }

    AnimatedContent(
        targetState = isOptionsVisible,
        transitionSpec = {
            slideInHorizontally(animationSpec = tween(durationMillis = 300)) togetherWith slideOutHorizontally(
                animationSpec = tween(durationMillis = 300)
            )
        }) { visible ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (visible) lightGray else gray)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.9f)
                        .height(120.dp)
                        .background(
                            if (!visible) lightGray else gray,
                            RoundedCornerShape(20.dp)
                        )
                ) {
                    if (!visible) {
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(petroleum),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Alarm,
                                contentDescription = "Alarma",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Text(
                            "$hour:$minute hs - Dosis $finalDosis",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(lightGray),
                                contentAlignment = Alignment.Center
                            )
                            {
                                Icon(
                                    Icons.Filled.DeleteForever,
                                    "Conteo de Pastillas",
                                    tint = pink,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clickable {
                                            onCancelAlarm(alarm.requestCode, viewModel.medicationName.value)
                                            viewModel.deleteAlarm(alarm)
                                        }

                                )
                            }
                        }
                    }
                }
                Icon(
                    Icons.Filled.KeyboardDoubleArrowLeft,
                    "Flecha",
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { isOptionsVisible = !visible })

            }
        }
    }
}