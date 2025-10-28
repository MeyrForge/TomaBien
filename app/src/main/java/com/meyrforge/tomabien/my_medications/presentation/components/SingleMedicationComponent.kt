package com.meyrforge.tomabien.my_medications.presentation.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander

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
                        }else{ onPillsOpened()}
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