package com.softartdev.notedelight.ui

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.printToLog
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import com.softartdev.notedelight.MainActivity
import com.softartdev.notedelight.shared.R
import com.softartdev.notedelight.shared.base.IdlingResource
import leakcanary.DetectLeaksAfterTestSuccess
import leakcanary.TestDescriptionHolder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import java.util.UUID

class EditTitleTest {

    private val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val rules: RuleChain = RuleChain.outerRule(TestDescriptionHolder)
        .around(DetectLeaksAfterTestSuccess())
        .around(composeTestRule)

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(IdlingResource.countingIdlingResource)
        composeTestRule.registerIdlingResource(composeIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        composeTestRule.unregisterIdlingResource(composeIdlingResource)
        IdlingRegistry.getInstance().unregister(IdlingResource.countingIdlingResource)
    }

    @Test
    fun editTitleAfterCreateTest() {
        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.create_note))
            .assertIsDisplayed()
            .performClick()

        val actualNoteText = UUID.randomUUID().toString().substring(0, 30)
        composeTestRule.onNodeWithText(text = context.getString(R.string.type_text))
            .assertIsDisplayed()
            .performTextInput(actualNoteText)

        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.action_edit_title))
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onAllNodes(isRoot()).printToLog("🦄", maxDepth = Int.MAX_VALUE)

        val actualNoteTitle = "title"
        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.enter_title))
            .assertIsDisplayed()
            .performTextReplacement(actualNoteTitle)

        composeTestRule.onNodeWithText(text = context.getString(R.string.yes))
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.enter_title))
            .assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.action_save_note))
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithContentDescription(label = Icons.Default.ArrowBack.name)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.safeWaitUntil {
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithContentDescription(label = actualNoteTitle)
                .assertIsDisplayed()
        }
        composeTestRule.onNodeWithContentDescription(label = actualNoteTitle)
            .assertIsDisplayed()
    }

    @Test
    fun editTitleAfterSaveTest() {
        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.create_note))
            .assertIsDisplayed()
            .performClick()

        val actualNoteText = UUID.randomUUID().toString().substring(0, 30)
        composeTestRule.onNodeWithText(text = context.getString(R.string.type_text))
            .assertIsDisplayed()
            .performTextInput(actualNoteText)

        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.action_save_note))
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithContentDescription(label = Icons.Default.ArrowBack.name)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.safeWaitUntil {
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithContentDescription(label = actualNoteText)
                .assertIsDisplayed()
        }
        composeTestRule.onNodeWithContentDescription(label = actualNoteText)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.action_edit_title))
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onAllNodes(isRoot()).printToLog("🦄", maxDepth = Int.MAX_VALUE)

        val actualNoteTitle = "title"
        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.enter_title))
            .assertIsDisplayed()
            .performTextReplacement(actualNoteTitle)

        composeTestRule.onNodeWithText(text = context.getString(R.string.yes))
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.enter_title))
            .assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription(label = context.getString(R.string.action_save_note))
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithContentDescription(label = Icons.Default.ArrowBack.name)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.safeWaitUntil {
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithContentDescription(label = actualNoteTitle)
                .assertIsDisplayed()
        }
        composeTestRule.onNodeWithContentDescription(label = actualNoteTitle)
            .assertIsDisplayed()
    }
}