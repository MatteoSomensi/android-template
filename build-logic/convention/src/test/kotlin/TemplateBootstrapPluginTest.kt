import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class TemplateBootstrapPluginTest {
    @Test
    fun `standard preset keeps production defaults`() {
        assertEquals(setOf("sync", "benchmark", "roborazzi"), resolveCapabilities("standard", null))
    }

    @Test
    fun `explicit capabilities override preset`() {
        assertEquals(setOf("firebase", "auth"), resolveCapabilities("minimal", "firebase,auth"))
    }

    @Test
    fun `auth requires firebase`() {
        assertThrows(IllegalArgumentException::class.java) { resolveCapabilities("standard", "auth") }
    }

    @Test
    fun `app name becomes a safe identifier`() {
        assertEquals("MyGreatApp", "My great-app".toPascalIdentifier())
        assertEquals("App42", "42".toPascalIdentifier())
    }

    @Test
    fun `replacement output is not processed a second time`() {
        val replacements =
            linkedMapOf(
                "MatteoSomensi/android-template" to "example/android-template-canary",
                "android-template" to "android-template-canary",
            )

        assertEquals(
            "example/android-template-canary",
            tokenReplacer(replacements)("MatteoSomensi/android-template"),
        )
    }

    @Test
    fun `internal marker cleanup preserves line endings and inline content`() {
        val input =
            "include(\":app\") // TEMPLATE_OPTIONAL_INLINE\r\n" +
                "// TEMPLATE_OPTIONAL_MODULES_START\r\n" +
                "include(\":feature\")\r\n" +
                "// TEMPLATE_OPTIONAL_MODULES_END\r\n"

        assertEquals(
            "include(\":app\") // TEMPLATE_OPTIONAL_INLINE\r\ninclude(\":feature\")\r\n",
            removeInternalMarkerLines(input),
        )
    }
}
