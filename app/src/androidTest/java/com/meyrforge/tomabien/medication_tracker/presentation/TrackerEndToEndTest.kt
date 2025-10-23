package com.meyrforge.tomabien.medication_tracker.presentation

import android.Manifest
import androidx.compose.ui.test.assertTextEquals
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
        composeRule.onNodeWithContentDescription("Agregar medicación").performClick()
        composeRule.onNodeWithTag(TestTags.NEW_MEDICATION_NAME).performTextInput("Ibuprofeno")
        composeRule.onNodeWithTag(TestTags.NEW_MEDICATION_GRAMMAGE).performTextInput("600")
        composeRule.onNodeWithTag(TestTags.OPTIONAL_CHECK).performClick()
        composeRule.onNodeWithTag(TestTags.ACTIVATE_PILL_CHECK).performClick()

        composeRule.onNodeWithText("Dosis").assertExists()

        composeRule.onNodeWithText("Dosis").performTextInput("1.5")
        composeRule.onNodeWithTag(TestTags.ADD_NEW_MEDICATION).performClick()

        composeRule.onNodeWithText("Ibuprofeno - 600").assertExists()

        composeRule.onNodeWithContentDescription("Conteo de Pastillas").performClick()

        composeRule.onNodeWithContentDescription("Cantidad de pastillas actual").assertTextEquals("0")

        composeRule.onNodeWithText("Nueva cantidad").performTextInput("20.5")
        composeRule.onNodeWithTag(TestTags.SAVE_PILL_AMOUNT).performClick()
    }

    @Test
    fun checkAndUncheckTrackerMultipleTimes_checkSummaryAfterwards(){
        composeRule.onNodeWithContentDescription("Alarma").performClick()
        composeRule.onNodeWithContentDescription("Agregar alarma").performClick()
        composeRule.onNodeWithTag(TestTags.ADD_ALARM).performClick()

        composeRule.onNodeWithContentDescription("Seguimiento", useUnmergedTree = true).performClick()

        composeRule.onNodeWithText("Ibuprofeno 600").assertExists()

        composeRule.onNodeWithTag(TestTags.TRACKER_CHECK).performClick() //Tomada
        composeRule.onNodeWithTag(TestTags.TRACKER_CHECK).performClick() //No tomada
        composeRule.onNodeWithTag(TestTags.TRACKER_CHECK).performClick() //Tomada
        composeRule.onNodeWithTag(TestTags.TRACKER_CHECK).performClick() //No Tomada

        composeRule.onNodeWithContentDescription("Resumen", useUnmergedTree = true).performClick()

        composeRule.onNodeWithText("No tomada").assertExists()

    }

    @Test
    fun checkTrackerMultipleTimes_PillCountShouldNotExtractDosage(){
        composeRule.onNodeWithContentDescription("Alarma").performClick()
        composeRule.onNodeWithContentDescription("Agregar alarma").performClick()
        composeRule.onNodeWithTag(TestTags.ADD_ALARM).performClick()

        composeRule.onNodeWithContentDescription("Seguimiento", useUnmergedTree = true).performClick()

        composeRule.onNodeWithText("Ibuprofeno 600").assertExists()

        composeRule.onNodeWithTag(TestTags.TRACKER_CHECK).performClick() //Tomada
        composeRule.onNodeWithTag(TestTags.TRACKER_CHECK).performClick() //No tomada
        composeRule.onNodeWithTag(TestTags.TRACKER_CHECK).performClick() //Tomada
        composeRule.onNodeWithTag(TestTags.TRACKER_CHECK).performClick() //No Tomada

        composeRule.onNodeWithContentDescription("Resumen", useUnmergedTree = true).performClick()

        composeRule.onNodeWithText("No tomada").assertExists()

        composeRule.onNodeWithContentDescription("Mis Medicaciones", useUnmergedTree = true).performClick()
        composeRule.onNodeWithContentDescription("Conteo de Pastillas").performClick()

        composeRule.onNodeWithContentDescription("Cantidad de pastillas actual").assertTextEquals("20.5")
    }
}