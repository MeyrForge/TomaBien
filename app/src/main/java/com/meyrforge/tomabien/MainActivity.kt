package com.meyrforge.tomabien

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.meyrforge.tomabien.common.Constants
import com.meyrforge.tomabien.common.Screen
import com.meyrforge.tomabien.common.alarm.AlarmReceiver
import com.meyrforge.tomabien.medication_tracker.presentation.MedicationTrackerScreen
import com.meyrforge.tomabien.my_medications.presentation.MedicationAlarmsScreen
import com.meyrforge.tomabien.my_medications.presentation.MyMedicationsScreen
import com.meyrforge.tomabien.ui.theme.NavBarColor
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
import com.meyrforge.tomabien.ui.theme.TomaBienTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()
        setContent {
            TomaBienTheme {
                var hasNotificationPermission by remember {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                }
                val permissionLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                        hasNotificationPermission = isGranted
                    }
                val navController = rememberNavController()

                Scaffold(bottomBar = { NavigationBarComponent(navController) }) {
                    Box(modifier = Modifier.padding(it)) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.MyMedications.route
                        ) {
                            composable(route = Screen.MyMedications.route) {
                                MyMedicationsScreen(navController)
                            }
                            composable(route = Screen.Alarms.route + "/{${Constants.MEDICATION_ID}}") {
                                MedicationAlarmsScreen(
                                    onSetAlarm = ::setAlarm, onCancelAlarm = ::cancelAlarm,
                                    launchPermission = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
                                )
                            }
                            composable(route = Screen.MedicationTracker.route) {
                                MedicationTrackerScreen()
                            }
                        }

                    }
                }
            }
        }
    }


    private fun createNotificationChannel() {
        val name: CharSequence = "TomaBienReminderChannel"
        val description = "Channel for alarm manager"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("TomaBien", name, importance)
        channel.description = description
        val notificationManager = getSystemService(NotificationManager::class.java)

        notificationManager.createNotificationChannel(channel)
    }

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    fun setAlarm(hour: Int, minute: Int, requestCode: Int, medName: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                // If the set time is in the past, advance to the next day
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("message", "Alarma de toma para $medName")
            intent.putExtra("alarm_id", requestCode)

            val pendingIntentFlags =
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            val pendingIntent =
                PendingIntent.getBroadcast(this, requestCode, intent, pendingIntentFlags)

            try {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
                Toast.makeText(this, "Alarm set successfully for $hour:$minute", Toast.LENGTH_LONG)
                    .show()
            } catch (e: SecurityException) {
                Toast.makeText(
                    this,
                    "Permission to schedule exact alarms not granted.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                this,
                "El permiso para mostrar notificaciones no fue otorgado",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun cancelAlarm(requestCode: Int) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        val pendingIntentFlags =
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent =
            PendingIntent.getBroadcast(this, requestCode, intent, pendingIntentFlags)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun NavigationBarComponent(navController: NavController) {
    var screen by remember { mutableStateOf(Screen.MyMedications.route) }
    NavigationBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
        containerColor = NavBarColor
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.AddTask, "Seguimiento") },
            selected = screen == Screen.MedicationTracker.route,
            label = { Text("Seguimiento") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = PowderedPink,
                unselectedTextColor = PowderedPink,
                indicatorColor = SoftBlueLavander,
                selectedTextColor = SoftBlueLavander
            ),
            onClick = {
                if(screen != Screen.MedicationTracker.route){
                    screen = Screen.MedicationTracker.route
                    navController.navigate(Screen.MedicationTracker.route)
                }
            })

        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Medication, "Mis Medicaciones") },
            selected = screen == Screen.MyMedications.route,
            label = { Text("Mis medicaciones") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = PowderedPink,
                unselectedTextColor = PowderedPink,
                indicatorColor = SoftBlueLavander,
                selectedTextColor = SoftBlueLavander
            ),
            onClick = {
                if(screen != Screen.MyMedications.route){
                    screen = Screen.MyMedications.route
                    navController.navigate(Screen.MyMedications.route)
                }
            })
    }
}
