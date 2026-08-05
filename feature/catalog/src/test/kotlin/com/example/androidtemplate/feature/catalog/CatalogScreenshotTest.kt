package com.example.androidtemplate.feature.catalog

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import com.example.androidtemplate.core.designsystem.TemplateAppTheme
import com.example.androidtemplate.core.testing.sampleCatalogItems
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class CatalogScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    @Config(qualifiers = "w400dp-h400dp")
    fun compactShort() = capture("compact-short")

    @Test
    @Config(qualifiers = "w400dp-h500dp")
    fun compactMedium() = capture("compact-medium")

    @Test
    @Config(qualifiers = "w400dp-h1000dp")
    fun compactTall() = capture("compact-tall")

    @Test
    @Config(qualifiers = "w610dp-h400dp")
    fun mediumShort() = capture("medium-short")

    @Test
    @Config(qualifiers = "w610dp-h500dp")
    fun mediumMedium() = capture("medium-medium")

    @Test
    @Config(qualifiers = "w610dp-h1000dp")
    fun mediumTall() = capture("medium-tall")

    @Test
    @Config(qualifiers = "w900dp-h400dp")
    fun expandedShort() = capture("expanded-short")

    @Test
    @Config(qualifiers = "w900dp-h500dp")
    fun expandedMedium() = capture("expanded-medium")

    @Test
    @Config(qualifiers = "w900dp-h1000dp")
    fun expandedTall() = capture("expanded-tall")

    @Test
    @Config(qualifiers = "w400dp-h500dp-night")
    fun darkTheme() = capture("compact-dark", darkTheme = true)

    @Test
    @Config(qualifiers = "w400dp-h500dp")
    fun largeFont() = capture("compact-font-150", fontScale = 1.5f)

    private fun capture(
        name: String,
        darkTheme: Boolean = false,
        fontScale: Float = 1f,
    ) {
        composeRule.setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale)) {
                TemplateAppTheme(darkTheme = darkTheme, dynamicColor = false) {
                    CatalogListScreen(sampleCatalogItems, onItemClick = {})
                }
            }
        }
        composeRule.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }
}
