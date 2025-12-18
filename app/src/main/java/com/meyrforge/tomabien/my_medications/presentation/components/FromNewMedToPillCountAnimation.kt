package com.meyrforge.tomabien.my_medications.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meyrforge.tomabien.ui.theme.gray
import com.meyrforge.tomabien.ui.theme.lightGray
import com.meyrforge.tomabien.ui.theme.petroleum
import com.meyrforge.tomabien.ui.theme.pink

@Preview
@Composable
fun FromNewMedToPillCountAnimation() {
    var isOptionsVisible by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = isOptionsVisible,
        transitionSpec = { fadeIn(animationSpec = tween(400)) + expandHorizontally() togetherWith fadeOut(animationSpec = tween(400)) + shrinkHorizontally() }) { visible ->
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
                        /*.background(
                            if (!visible) lightGray else gray,
                            RoundedCornerShape(20.dp)
                        )*/
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
                            "hola",
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
                                    .background(lightGray.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            )
                            {
                                Icon(
                                    Icons.Filled.DeleteForever,
                                    "Conteo de Pastillas",
                                    tint = pink,
                                    modifier = Modifier
                                        .size(35.dp)

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