package com.meyrforge.tomabien.my_medications.presentation.components

import android.widget.Toast
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
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.meyrforge.tomabien.R
import com.meyrforge.tomabien.common.Screen
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.MedicationViewModel
import com.meyrforge.tomabien.ui.theme.LightWarmGray
import com.meyrforge.tomabien.ui.theme.NavBarColor
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
import com.meyrforge.tomabien.ui.theme.gray
import com.meyrforge.tomabien.ui.theme.lightGray
import com.meyrforge.tomabien.ui.theme.petroleum

@Composable
fun SingleMedicationComponentOld(
    navController: NavController,
    viewModel: MedicationViewModel = hiltViewModel(),
    med: Medication,
    onEdit: () -> Unit,
    onPillsOpened: () -> Unit
) {
    val context = LocalContext.current
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Album,
                "Opcional",
                tint = if (med.optional) SoftBlueLavander else NavBarColor,
                modifier = Modifier.weight(1f)
            )
            Text(
                "${med.name} - ${med.grammage}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PowderedPink,
                modifier = Modifier.weight(6f)
            )
            Icon(
                painterResource(id = R.drawable.ic_blister_xml),
                "Conteo de Pastillas",
                tint = if (!med.countActivated) NavBarColor else PowderedPink,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (!med.countActivated) {
                            Toast.makeText(
                                context,
                                "Conteo de pastillas desactivado",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onPillsOpened()
                        }
                    }
            )
            Icon(
                Icons.Outlined.Edit,
                "Editar",
                tint = LightWarmGray,
                modifier = Modifier
                    .weight(2f)
                    .clickable {
                        onEdit()
                    }
            )
            Icon(
                Icons.Outlined.Alarm,
                "Alarma",
                tint = SoftBlueLavander,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        navController.navigate(Screen.Alarms.route + "/${med.id}")
                    }
            )
        }
        HorizontalDivider(thickness = 2.dp, color = PowderedPink)
    }
}

@Composable
fun SingleMedicationComponent(
    navController: NavController,
    viewModel: MedicationViewModel = hiltViewModel(),
    med: Medication,
    onEdit: () -> Unit,
    onPillsOpened: () -> Unit
) {
    val context = LocalContext.current
    var isOptionsVisible by remember { mutableStateOf(false) }
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
                                .background(if (med.optional) petroleum else gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_pills),
                                contentDescription = "Pastillas",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Text(
                            "${med.name} - ${med.grammage}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(12.dp)
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(lightGray),
                                contentAlignment = Alignment.Center
                            )
                            {
                                Icon(
                                    painterResource(id = R.drawable.ic_blister_xml),
                                    "Conteo de Pastillas",
                                    tint = if (!med.countActivated) NavBarColor else Color.White,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable {
                                            if (!med.countActivated) {
                                                Toast.makeText(
                                                    context,
                                                    "Conteo de pastillas desactivado",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                onPillsOpened()
                                            }
                                        }

                                )
                            }
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(lightGray),
                                contentAlignment = Alignment.Center
                            )
                            {
                                Icon(
                                    Icons.Outlined.Edit,
                                    "Editar",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable {
                                            onEdit()
                                        }
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(lightGray),
                                contentAlignment = Alignment.Center
                            )
                            {
                                Icon(
                                    Icons.Outlined.Alarm,
                                    "Alarma",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable {
                                            navController.navigate(Screen.Alarms.route + "/${med.id}")
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