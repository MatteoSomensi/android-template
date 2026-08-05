package com.example.androidtemplate.core.network

import com.example.androidtemplate.core.model.CatalogItem
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface CatalogNetworkDataSource {
    suspend fun items(
        offset: Int,
        limit: Int,
    ): List<CatalogItem>
}

@Serializable
data class NetworkCatalogItem(
    val id: Long,
    val title: String,
    val summary: String,
)

interface CatalogApi {
    @GET("items")
    suspend fun items(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): List<NetworkCatalogItem>
}

class RetrofitCatalogNetworkDataSource
    @Inject
    constructor(
        private val api: CatalogApi,
    ) : CatalogNetworkDataSource {
        override suspend fun items(
            offset: Int,
            limit: Int,
        ): List<CatalogItem> = api.items(offset, limit).map { CatalogItem(it.id, it.title, it.summary) }
    }

class FixtureCatalogNetworkDataSource
    @Inject
    constructor() : CatalogNetworkDataSource {
        private val catalog =
            List(60) { index ->
                val id = index + 1L
                CatalogItem(
                    id = id,
                    title = "Reference item $id",
                    summary = "Deterministic content for architecture, UI, and test examples.",
                )
            }

        override suspend fun items(
            offset: Int,
            limit: Int,
        ): List<CatalogItem> = catalog.drop(offset).take(limit)
    }
