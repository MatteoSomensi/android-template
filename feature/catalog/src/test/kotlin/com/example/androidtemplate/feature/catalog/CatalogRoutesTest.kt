package com.example.androidtemplate.feature.catalog

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogRoutesTest {
    @Test
    fun routesSurviveBackStackSerialization() {
        val list = Json.decodeFromString<CatalogList>(Json.encodeToString(CatalogList))
        val detail = CatalogDetail(id = 42)
        val restoredDetail = Json.decodeFromString<CatalogDetail>(Json.encodeToString(detail))

        assertEquals(CatalogList, list)
        assertEquals(detail, restoredDetail)
    }
}
