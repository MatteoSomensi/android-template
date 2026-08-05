package com.example.androidtemplate.core.testing

import androidx.paging.PagingData
import com.example.androidtemplate.core.domain.CatalogRepository
import com.example.androidtemplate.core.model.CatalogItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeCatalogRepository(
    initialItems: List<CatalogItem> = sampleCatalogItems,
) : CatalogRepository {
    private val items = MutableStateFlow(initialItems)

    override fun pagedItems(): Flow<PagingData<CatalogItem>> = flowOf(PagingData.from(items.value))

    override fun observeItem(id: Long): Flow<CatalogItem?> = MutableStateFlow(items.value.firstOrNull { it.id == id })

    override suspend fun refresh() = Unit
}

val sampleCatalogItems =
    listOf(
        CatalogItem(1, "Architecture", "A deterministic reference item."),
        CatalogItem(2, "Testing", "Fakes keep local tests fast and reliable."),
        CatalogItem(3, "Delivery", "CI and CD are part of the template contract."),
    )
