package com.meyrforge.tomabien.ui.sharedComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander

@Composable
fun SegmentedButtonComponent(
    modifier: Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = { onCheckedChange(!checked) },
        enabled = true,
        thumbContent = if (checked) {
            {
                Text(
                    "SI",
                    fontSize = 10.sp
                )
            }
        } else {
            {
                Text(
                    "NO",
                    fontSize = 10.sp
                )
            }
        },
        colors = SwitchDefaults.colors(
            checkedTrackColor = PowderedPink,
            checkedThumbColor = Color.White,
            checkedIconColor = DeepPurple,
            disabledCheckedTrackColor = Color.Gray,
            disabledUncheckedTrackColor = Color.Gray,
            uncheckedBorderColor = Color.Transparent,
            uncheckedThumbColor = SoftBlueLavander

        ),
        modifier = modifier
    )

}

@Composable
fun QuestionWithSegmentedButtonComponent(text:String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, fontSize = 16.sp, color = PowderedPink, modifier = Modifier.weight(8f))
        SegmentedButtonComponent(modifier = Modifier.weight(2f), checked, onCheckedChange)
    }
}