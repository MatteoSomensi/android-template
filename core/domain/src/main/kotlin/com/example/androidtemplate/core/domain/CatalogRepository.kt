package com.example.androidtemplate.core.domain

import androidx.paging.PagingData
import com.example.androidtemplate.core.model.CatalogItem
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    fun pagedItems(): Flow<PagingData<CatalogItem>>

    fun observeItem(id: Long): Flow<CatalogItem?>

    suspend fun refresh()
}
