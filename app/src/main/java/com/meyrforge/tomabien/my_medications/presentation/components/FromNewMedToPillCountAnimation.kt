package com.meyrforge.tomabien.my_medications.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.meyrforge.tomabien.R
import com.meyrforge.tomabien.common.TestTags
import com.meyrforge.tomabien.ui.theme.DeepPurple
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
import com.meyrforge.tomabien.ui.theme.pink

@Preview
@Composable
fun FromNewMedToPillCountAnimation() {
    var isVisible by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .clip(shape = CircleShape)
                .background(if (isVisible) pink else DeepPurple)
                .size(50.dp)
                .wrapContentSize(Alignment.Center)
        )
        AnimatedContent(
            targetState = isVisible,
            transitionSpec = { fadeIn().togetherWith(fadeOut()) },
            content = { visible ->
                if (visible) {
                    AlertDialog(
                        icon = { Icon(Icons.Outlined.Alarm, "Alarma") },
                        title = { Text("Agregar medicación") },
                        text = {},
                        onDismissRequest = {},
                        confirmButton = {
                            TextButton(onClick = { isVisible = !isVisible })
                            { Text("Agregar", color = PowderedPink) }

                        },
                        dismissButton = {
                            TextButton(
                                onClick = {}
                            ) {
                                Text("Cancelar", color = PowderedPink)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = DeepPurple,
                        iconContentColor = SoftBlueLavander,
                        titleContentColor = PowderedPink,
                        textContentColor = PowderedPink,
                        tonalElevation = 10.dp
                    )
                } else {

                    AlertDialog(
                        icon = { Icon(Icons.Outlined.Alarm, "Alarma") },
                        title = { Text("Agregar alarma") },
                        text = {},
                        onDismissRequest = {},
                        confirmButton = {
                            TextButton(onClick = { isVisible = !isVisible })
                            { Text("Agregar", color = PowderedPink) }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {}
                            ) {
                                Text("Cancelar", color = PowderedPink)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = DeepPurple,
                        iconContentColor = SoftBlueLavander,
                        titleContentColor = PowderedPink,
                        textContentColor = PowderedPink,
                        tonalElevation = 10.dp
                    )
                }
            })
    }
}