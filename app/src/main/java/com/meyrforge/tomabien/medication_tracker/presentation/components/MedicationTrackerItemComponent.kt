package com.meyrforge.tomabien.medication_tracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Square
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.common.TestTags
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import com.meyrforge.tomabien.medication_tracker.presentation.MedicationTrackerViewModel
import com.meyrforge.tomabien.ui.theme.LightWarmGray
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
import com.meyrforge.tomabien.ui.theme.gray
import com.meyrforge.tomabien.ui.theme.green
import com.meyrforge.tomabien.ui.theme.lightGray
import com.meyrforge.tomabien.ui.theme.pink
import java.util.Calendar

@Composable
fun MedicationTrackerItemComponentOld(
    medId: Int,
    hour: String,
    medName: String,
    viewModel: MedicationTrackerViewModel = hiltViewModel()
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val formattedDay = day.toString().padStart(2, '0')
    val formattedMonth = month.toString().padStart(2, '0')

    val date = "$formattedDay/$formattedMonth/$year"
    val trackerList = viewModel.medicationTrackerList.value
    var wasTaken by remember {
        mutableStateOf(
            trackerList?.find { it.medicationId == medId && it.date == date && it.hour == hour }?.taken
                ?: false
        )
    }


    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                hour,
                fontSize = 24.sp,
                color = LightWarmGray
            )
            Text(
                medName,
                fontSize = 20.sp,
                color = PowderedPink
            )
            Checkbox(
                wasTaken,
                {
                    wasTaken = it
                    viewModel.addTrackerToList(
                        MedicationTracker(
                            id = null,
                            date = date,
                            hour = hour,
                            medicationId = medId,
                            taken = it
                        )
                    )
                },
                colors = CheckboxDefaults.colors(checkedColor = PowderedPink),
                modifier = Modifier.testTag(TestTags.TRACKER_CHECK)
            )
        }
        HorizontalDivider(thickness = 2.dp, color = SoftBlueLavander)
    }
}

@Composable
fun MedicationTrackerItemComponent(
    medId: Int,
    hour: String,
    medName: String,
    viewModel: MedicationTrackerViewModel = hiltViewModel()
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val formattedDay = day.toString().padStart(2, '0')
    val formattedMonth = month.toString().padStart(2, '0')

    val date = "$formattedDay/$formattedMonth/$year"
    val trackerList = viewModel.medicationTrackerList.value
    var wasTaken by remember {
        mutableStateOf(
            trackerList?.find { it.medicationId == medId && it.date == date && it.hour == hour }?.taken
                ?: false
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(gray.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.weight(7f)) {
            Text(
                hour,
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
            Text(
                medName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        CustomCheckBox(
            wasTaken
        ) {
            wasTaken = !wasTaken
            viewModel.addTrackerToList(
                MedicationTracker(
                    id = null,
                    date = date,
                    hour = hour,
                    medicationId = medId,
                    taken = wasTaken
                )
            )
        }

    }
}

@Preview
@Composable
fun CustomCheckBox(checked: Boolean = false, onCheckedChange: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(if (checked) green else Color.Transparent, CircleShape)
            .border(2.dp, if (checked) Color.Transparent else Color.White, CircleShape)
            .clickable{onCheckedChange()},
        contentAlignment = Alignment.Center
    ) {
        if (checked)
            Icon(
                Icons.Outlined.Check,
                "Tomada",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
    }
}