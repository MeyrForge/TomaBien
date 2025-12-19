package com.meyrforge.tomabien.weekly_summary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ChipColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meyrforge.tomabien.ui.theme.pink

@Preview
@Composable
fun PersonalizedChipComponent(color: Color = pink, text: String = "Tomada", icon: ImageVector = Icons.Filled.CheckCircle) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .padding(4.dp)
    ) {
        Icon(icon, "Fue tomada", tint = Color.White.copy(alpha = 0.5f))
        Text(text, color = Color.White, modifier = Modifier.padding(4.dp))
    }
}