package com.example.androidtemplate.feature.catalog

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.androidtemplate.core.designsystem.TemplateAppTheme
import com.example.androidtemplate.core.testing.sampleCatalogItems
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class CatalogScreenBehaviorTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun selectingAnItemReportsItsStableId() {
        var selectedId: Long? = null
        composeRule.setContent {
            TemplateAppTheme(dynamicColor = false) {
                CatalogListScreen(sampleCatalogItems, onItemClick = { selectedId = it })
            }
        }

        composeRule.onNodeWithText("Testing").performClick()

        assertEquals(2L, selectedId)
    }

    @Test
    fun detailBackButtonIsOnlyShownInSinglePaneMode() {
        composeRule.setContent {
            TemplateAppTheme(dynamicColor = false) {
                CatalogDetailScreen(sampleCatalogItems.first(), showBackButton = false, onBack = {})
            }
        }

        composeRule.onAllNodesWithText("Architecture").assertCountEquals(2)
        composeRule.onNodeWithContentDescription("Back").assertDoesNotExist()
    }
}
