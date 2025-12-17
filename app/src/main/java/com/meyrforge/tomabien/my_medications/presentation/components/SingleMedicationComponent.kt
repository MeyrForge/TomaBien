package com.meyrforge.tomabien.my_medications.presentation.components

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.meyrforge.tomabien.R
import com.meyrforge.tomabien.common.Screen
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.MedicationViewModel
import com.meyrforge.tomabien.ui.theme.LightWarmGray
import com.meyrforge.tomabien.ui.theme.NavBarColor
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.PurpleGrey40
import com.meyrforge.tomabien.ui.theme.PurpleGrey80
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
import com.meyrforge.tomabien.ui.theme.gray
import com.meyrforge.tomabien.ui.theme.lightGray
import com.meyrforge.tomabien.ui.theme.petroleum
import com.meyrforge.tomabien.ui.theme.purple

@Composable
fun SingleMedicationComponent(
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

@Preview
@Composable
fun SingleMedicationComponentPreview() {
    var isOptionsVisible by remember { mutableStateOf(false) }
    AnimatedContent(
        targetState = isOptionsVisible,
        transitionSpec = {
            slideInHorizontally { fullHeight -> fullHeight } togetherWith slideOutHorizontally { fullHeight -> -fullHeight }
        }) { visible ->
        if (!visible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(gray)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.9f)
                            .height(120.dp)
                            .background(
                                lightGray,
                                RoundedCornerShape(20.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(gray),
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
                            "Paracetamol 600mg",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Icon(
                        Icons.Filled.KeyboardDoubleArrowLeft,
                        "Flecha",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isOptionsVisible = true })
                }

            }
        } else {
            Box(contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(gray)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.KeyboardDoubleArrowRight,
                        "Flecha",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isOptionsVisible = false })
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.9f)
                            .height(120.dp)
                            .background(
                                lightGray,
                                RoundedCornerShape(20.dp)
                            )
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_blister_xml),
                            "Conteo de Pastillas",
                            tint = purple
                        )
                        Icon(
                            Icons.Outlined.Edit,
                            "Editar",
                            tint = Color.White
                        )
                        Icon(
                            Icons.Outlined.Alarm,
                            "Alarma",
                            tint = petroleum
                        )
                    }
                }
            }

        }
    }
}