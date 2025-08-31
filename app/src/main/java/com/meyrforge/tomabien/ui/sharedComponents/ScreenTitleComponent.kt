package com.meyrforge.tomabien.ui.sharedComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink

@Preview
@Composable
fun ScreenTitleComponent(text: String = "Titulo") {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(bottom = 24.dp)
            .background(PowderedPink)
            .padding(16.dp)
    ) {
        Text(
            text, color = DeepPurple, fontSize = 28.sp, fontWeight = FontWeight.Bold
        )
    }
}