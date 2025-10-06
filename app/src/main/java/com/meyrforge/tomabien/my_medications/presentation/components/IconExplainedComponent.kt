package com.meyrforge.tomabien.my_medications.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meyrforge.tomabien.R
import com.meyrforge.tomabien.ui.theme.PowderedPink

@Composable
fun IconExplainedComponent(
    icon: ImageVector = Icons.Outlined.Album,
    text: String,
    tint: Color,
    isBlister: Boolean = false,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isBlister)
            Icon(
                painterResource(id = R.drawable.ic_blister_xml),
                "Blister",
                tint = tint,
                modifier = Modifier
                    .weight(1f)
                    .size(16.dp)
            )
        else
            Icon(
                icon,
                text,
                tint = tint,
                modifier = Modifier
                    .weight(1f)
                    .size(16.dp)
            )
        Text(
            text,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = PowderedPink,
            modifier = Modifier.weight(6f)
        )
    }
}