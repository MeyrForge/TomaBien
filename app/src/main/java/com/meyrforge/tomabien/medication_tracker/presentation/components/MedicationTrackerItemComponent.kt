package com.meyrforge.tomabien.medication_tracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meyrforge.tomabien.common.TestTags
import com.meyrforge.tomabien.common.getDayOfWeek
import com.meyrforge.tomabien.common.getMonth
import com.meyrforge.tomabien.medication_tracker.presentation.MedicationTrackerViewModel
import com.meyrforge.tomabien.ui.theme.LightWarmGray
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
import java.util.Calendar

@Composable
fun MedicationTrackerItemComponent(
    medId: Int,
    hour: String,
    medName: String,
    viewModel: MedicationTrackerViewModel = hiltViewModel()
) {
    var wasTaken by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)+1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val formattedDay = day.toString().padStart(2, '0')
    val formattedMonth = month.toString().padStart(2, '0')

    val date = "$formattedDay/$formattedMonth/$year"
    val trackerList by viewModel.medicationTrackerList.observeAsState()

    for (tracker in trackerList?:emptyList()){
        if (tracker.medicationId == medId && tracker.date == date && tracker.hour == hour){
            wasTaken = tracker.taken
        }
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
                    viewModel.saveOrEditMedicationTracker(medId, date, hour, wasTaken)
                },
                colors = CheckboxDefaults.colors(checkedColor = PowderedPink),
                modifier = Modifier.testTag(TestTags.TRACKER_CHECK)
            )
        }
        HorizontalDivider(thickness = 2.dp, color = SoftBlueLavander)
    }
}

private fun currentDay(): String {
    val calendar = Calendar.getInstance()
    return "${getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))}," +
            " ${calendar.get(Calendar.DAY_OF_MONTH)} de" +
            " ${getMonth(calendar.get(Calendar.MONTH))}"
}