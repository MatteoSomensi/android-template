package com.example.androidtemplate

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class DeepLinkTest {
    @Test
    fun validCatalogLinkCreatesAnItemId() {
        assertEquals(42L, Uri.parse("starter://catalog/42").toCatalogItemId())
    }

    @Test
    fun invalidCatalogLinkIsRejected() {
        assertNull(Uri.parse("starter://other/42").toCatalogItemId())
        assertNull(Uri.parse("starter://catalog/-1").toCatalogItemId())
    }
}
