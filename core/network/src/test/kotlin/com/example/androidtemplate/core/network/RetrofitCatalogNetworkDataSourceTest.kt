package com.example.androidtemplate.core.network

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RetrofitCatalogNetworkDataSourceTest {
    private val server = MockWebServer()

    @Before
    fun start() = server.start()

    @After
    fun stop() = server.shutdown()

    @Test
    fun `maps the network contract`() =
        runTest {
            server.enqueue(MockResponse().setBody("""[{"id":7,"title":"Seven","summary":"Fixture"}]"""))
            val api =
                Retrofit
                    .Builder()
                    .baseUrl(server.url("/"))
                    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                    .build()
                    .create(CatalogApi::class.java)

            val result = RetrofitCatalogNetworkDataSource(api).items(offset = 0, limit = 20)

            assertEquals(7L, result.single().id)
            assertEquals("offset=0&limit=20", server.takeRequest().requestUrl?.query)
        }
}
