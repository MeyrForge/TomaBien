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
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.meyrforge.tomabien.common.Constants
import com.meyrforge.tomabien.common.Screen
import com.meyrforge.tomabien.common.alarm.AlarmReceiver
import com.meyrforge.tomabien.medication_tracker.presentation.MedicationTrackerScreen
import com.meyrforge.tomabien.medication_tracker.presentation.MedicationTrackerViewModel
import com.meyrforge.tomabien.my_medications.domain.models.Medication
import com.meyrforge.tomabien.my_medications.presentation.MedicationAlarmsScreen
import com.meyrforge.tomabien.my_medications.presentation.MyMedicationsScreen
import com.meyrforge.tomabien.ui.theme.NavBarColor
import com.meyrforge.tomabien.ui.theme.PowderedPink
import com.meyrforge.tomabien.ui.theme.SoftBlueLavander
import com.meyrforge.tomabien.ui.theme.TomaBienTheme
import com.meyrforge.tomabien.weekly_summary.presentation.WeeklySummaryScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val medicationTrackerViewModel: MedicationTrackerViewModel by viewModels()

    @Inject
    lateinit var alarmManager: AlarmManager

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannels()
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
                val viewModel: MedicationTrackerViewModel = viewModel()
                LaunchedEffect(Unit) {
                    viewModel.getMedicationWithAlarms()
                }

                val medicationList by viewModel.medicationList.observeAsState(initial = emptyList())

                LaunchedEffect(medicationList.isNotEmpty()) {
                    if (medicationList.isNotEmpty()) {
                        // 1. Usamos .filter() para encontrar TODAS las medicaciones con pocas pastillas.
                        val medicationsToNotify = medicationList
                            .mapNotNull { it.medication }
                            .filter { med ->
                                med.numberOfPills != -1f && med.numberOfPills < 5 && med.numberOfPills > 0
                            }

                        // 2. Si la lista resultante no está vacía, mostramos la notificación.
                        if (medicationsToNotify.isNotEmpty()) {
                            if (checkNotificationPermission()) {
                                showLowPillsNotification(medicationsToNotify) // Pasamos la lista completa
                            }
                        }
                    }
                }

                Scaffold(bottomBar = { NavigationBarComponent(navController) }) {
                    Box(modifier = Modifier.padding(it)) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.MyMedications.route
                        ) {
                            composable(route = Screen.MyMedications.route) {
                                MyMedicationsScreen(navController, ::cancelAlarm)
                            }
                            composable(route = Screen.Alarms.route + "/{${Constants.MEDICATION_ID}}") {
                                MedicationAlarmsScreen(
                                    navController = navController,
                                    onSetAlarm = ::setAlarm, onCancelAlarm = ::cancelAlarm,
                                    launchPermission = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
                                )
                            }
                            composable(route = Screen.MedicationTracker.route) {
                                MedicationTrackerScreen(navController = navController)
                            }
                            composable(route = Screen.WeeklySummary.route) {
                                WeeklySummaryScreen(navController = navController)
                            }
                        }

                    }
                }
            }
        }
    }

    private fun showLowPillsNotification(medications: List<Medication>) {
        val notificationId =
            1001 // Usamos un ID fijo para que la notificación se sobreescriba y no se acumulen.

        // Creamos el texto detallado para la notificación.
        val notificationText = medications.joinToString(separator = "\n") { med ->
            "• ${med.name}: Quedan ${med.numberOfPills} pastillas."
        }

        // Creamos un estilo de notificación expandible.
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .setBigContentTitle("Pocas pastillas restantes") // Título cuando está expandida
            .bigText(notificationText) // El texto detallado

        val builder = NotificationCompat.Builder(this, TomaBienApplication.LOW_PILLS_CHANNEL_ID)
            .setSmallIcon(R.drawable.new_tb_small_icon)
            .setContentTitle("Pocas pastillas restantes") // Título cuando está contraída
            .setContentText("Tienes medicaciones con pocas unidades.") // Texto genérico para la vista contraída
            .setStyle(bigTextStyle) // Aplicamos el estilo expandible
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (checkNotificationPermission())
                notify(notificationId, builder.build())
        }
    }

    private fun checkNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }


    private fun createNotificationChannels() {
        val notificationManager = getSystemService(NotificationManager::class.java)

        val reminderChannel = NotificationChannel(
            "TomaBien",
            "Recordatorios de Toma",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Canal para las alarmas de toma de medicación"
        }

        val lowPillsChannel = NotificationChannel(
            TomaBienApplication.LOW_PILLS_CHANNEL_ID,
            "Alertas de Medicación",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones para pocas pastillas restantes"
        }

        notificationManager.createNotificationChannel(reminderChannel)
        notificationManager.createNotificationChannel(lowPillsChannel)
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
                set(Calendar.MILLISECOND, 0)


                add(Calendar.DAY_OF_YEAR, 1)

            }

            val intent = Intent(this, AlarmReceiver::class.java).apply {
                putExtra("message", "¡Hora de tu medicina! Es momento de tomar $medName.")
                putExtra("alarm_id", requestCode)
            }

            val pendingIntentFlags =
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            val pendingIntent =
                PendingIntent.getBroadcast(this, requestCode, intent, pendingIntentFlags)

            try {
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
                val hourFormatted = if (hour.toString().length == 1) {
                    "0$hour"
                } else hour.toString()
                val minuteFormatted = if (minute.toString().length == 1) {
                    "0$minute"
                } else minute.toString()
                Toast.makeText(
                    this,
                    "Alarma programada para las $hourFormatted:$minuteFormatted, todos los días.",
                    Toast.LENGTH_LONG
                )
                    .show()
            } catch (e: SecurityException) {
                Toast.makeText(
                    this,
                    "Algo salió mal. Inténtalo de nuevo",
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

    fun cancelAlarm(requestCode: Int, medName: String) {
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("message", "¡Hora de tu medicina! Es momento de tomar $medName.")
            putExtra("alarm_id", requestCode)
        }


        val pendingIntentFlags =
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent =
            PendingIntent.getBroadcast(this, requestCode, intent, pendingIntentFlags)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarma cancelada", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun NavigationBarComponent(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
        containerColor = NavBarColor
    ) {
        val trackerScreen = Screen.MedicationTracker
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.AddTask, "Seguimiento") },
            selected = currentDestination?.hierarchy?.any { it.route == trackerScreen.route } == true,
            label = { Text("Seguimiento") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = PowderedPink,
                unselectedTextColor = PowderedPink,
                indicatorColor = SoftBlueLavander,
                selectedTextColor = SoftBlueLavander
            ),
            onClick = {
                navController.navigate(trackerScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })

        val summaryScreen = Screen.WeeklySummary
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Summarize, "Resumen") },
            selected = currentDestination?.hierarchy?.any { it.route == summaryScreen.route } == true,
            label = { Text("Resumen de Toma") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = PowderedPink,
                unselectedTextColor = PowderedPink,
                indicatorColor = SoftBlueLavander,
                selectedTextColor = SoftBlueLavander
            ),
            onClick = {
                navController.navigate(summaryScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })

        val myMedsScreen = Screen.MyMedications
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Medication, "Mis Medicaciones") },
            selected = currentDestination?.hierarchy?.any { it.route == myMedsScreen.route } == true,
            label = { Text("Mis medicaciones") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = PowderedPink,
                unselectedTextColor = PowderedPink,
                indicatorColor = SoftBlueLavander,
                selectedTextColor = SoftBlueLavander
            ),
            onClick = {
                navController.navigate(myMedsScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
    }
}
