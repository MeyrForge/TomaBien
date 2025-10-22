package com.meyrforge.tomabien

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.icu.util.Calendar
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import javax.inject.Inject

/*@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {
    // Regla para inicializar Hilt antes de cada test
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // Regla para lanzar la MainActivity
    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule(order = 2)
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        // Permiso necesario para que la app muestre notificaciones (Android 13+)
        Manifest.permission.POST_NOTIFICATIONS,
        // Añadimos también el de alarmas exactas por si lo usaras en el futuro
        Manifest.permission.SCHEDULE_EXACT_ALARM
    )

    // Hilt inyectará el mock que creamos en TestAppModule
    @Inject
    lateinit var mockAlarmManager: AlarmManager

    // Captor para "atrapar" los argumentos pasados a un métod del mock
    @Captor
    private lateinit var pendingIntentCaptor: ArgumentCaptor<PendingIntent>

    @Captor
    private lateinit var timeCaptor: ArgumentCaptor<Long>

    @Before
    fun setup() {
        // Inicializa Hilt para poder inyectar dependencias y los @Captor
        hiltRule.inject()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun setAlarm_shouldScheduleInexactRepeatingAlarm_correctly() {
        // Arrange: Preparamos los datos para la alarma
        val context = ApplicationProvider.getApplicationContext<Context>()
        val hour = 8
        val minute = 30
        val requestCode = 123
        val medName = "Ibuprofeno"

        // Calculamos el tiempo esperado (la misma lógica que en la app)
        val expectedCalendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        val expectedTriggerTime = expectedCalendar.timeInMillis

        // Act: Ejecutamos la función a probar dentro del hilo de la UI
        activityRule.scenario.onActivity { activity ->
            activity.setAlarm(hour, minute, requestCode, medName)
        }

        // Assert: Verificamos que el métod fue llamado en nuestro mock
        verify(mockAlarmManager).setInexactRepeating(
            eq(AlarmManager.RTC_WAKEUP),
            timeCaptor.capture(), // Capturamos el tiempo en lugar de compararlo
            eq(AlarmManager.INTERVAL_DAY),
            pendingIntentCaptor.capture()
        )

        val capturedTime = timeCaptor.value
        val toleranceMillis = 1000 // Aceptamos una diferencia de hasta 1 segundo
        Assert.assertTrue(
            "El tiempo de disparo ($capturedTime) no está dentro del rango esperado ($expectedTriggerTime)",
            kotlin.math.abs(expectedTriggerTime - capturedTime) < toleranceMillis
        )

        // (Opcional pero recomendado) Verificar el contenido del PendingIntent
        val capturedIntent = pendingIntentCaptor.value
        // Esta comprobación puede ser más compleja, pero por ahora nos aseguramos que no es nulo.
        assert(capturedIntent != null)
    }
}*/