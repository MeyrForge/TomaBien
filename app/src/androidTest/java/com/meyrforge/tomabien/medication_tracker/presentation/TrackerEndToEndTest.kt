package com.meyrforge.tomabien.medication_tracker.presentation

import android.Manifest
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.rule.GrantPermissionRule
import com.meyrforge.tomabien.MainActivity
import com.meyrforge.tomabien.common.Constants
import com.meyrforge.tomabien.common.Screen
import com.meyrforge.tomabien.common.TestTags
import com.meyrforge.tomabien.my_medications.presentation.MedicationAlarmsScreen
import com.meyrforge.tomabien.my_medications.presentation.MyMedicationsScreen
import com.meyrforge.tomabien.ui.theme.TomaBienTheme
import com.meyrforge.tomabien.weekly_summary.presentation.WeeklySummaryScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TrackerEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.POST_NOTIFICATIONS
    )

    @Before
    fun setUp() {
        hiltRule.inject()
        /*composeRule.setContent {
            TomaBienTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.MyMedications.route
                ) {
                    composable(route = Screen.MyMedications.route) {
                        MyMedicationsScreen(navController)
                    }
                    composable(route = Screen.MedicationTracker.route) {
                        MedicationTrackerScreen()
                    }
                    composable(route = Screen.Alarms.route + "/{${Constants.MEDICATION_ID}}") {
                        MedicationAlarmsScreen(
                            onSetAlarm = {_, _, _, _ -> }, onCancelAlarm = {_, _ -> },
                            launchPermission = {}
                        )
                    }
                    composable(route = Screen.WeeklySummary.route) {
                        WeeklySummaryScreen()
                    }
                }
            }
        }*/
    }

    @Test
    fun checkAndUncheckTrackerMultipleTimes_checkSummaryAfterwards(){
        composeRule.onNodeWithContentDescription("Agregar medicación").performClick()
        composeRule.onNodeWithTag(TestTags.NEW_MEDICATION_NAME).performTextInput("Ibuprofeno")
        composeRule.onNodeWithTag(TestTags.NEW_MEDICATION_GRAMMAGE).performTextInput("600")
        composeRule.onNodeWithTag(TestTags.ADD_NEW_MEDICATION).performClick()
        composeRule.onNodeWithContentDescription("Alarma").performClick()
        composeRule.onNodeWithContentDescription("Agregar alarma").performClick()
        composeRule.onNodeWithTag(TestTags.ADD_ALARM).performClick()

        composeRule.onNodeWithContentDescription("Seguimiento", useUnmergedTree = true).performClick()

        composeRule.onNodeWithText("Ibuprofeno 600").assertExists()

    }
}