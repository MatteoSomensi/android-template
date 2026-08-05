package com.example.androidtemplate.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CatalogDaoTest {
    private lateinit var database: TemplateDatabase

    @Before
    fun createDatabase() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), TemplateDatabase::class.java).build()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun upsertAndObserve() =
        runTest {
            database.catalogDao().upsertAll(listOf(CatalogEntity(1, "One", "Summary")))
            assertEquals(
                "One",
                database
                    .catalogDao()
                    .observe(1)
                    .first()
                    ?.title,
            )
        }
}
