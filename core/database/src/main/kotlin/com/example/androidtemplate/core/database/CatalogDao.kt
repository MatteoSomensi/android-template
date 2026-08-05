package com.example.androidtemplate.core.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {
    @Query("SELECT * FROM catalog_items ORDER BY id")
    fun pagingSource(): PagingSource<Int, CatalogEntity>

    @Query("SELECT * FROM catalog_items WHERE id = :id")
    fun observe(id: Long): Flow<CatalogEntity?>

    @Query("SELECT COUNT(*) FROM catalog_items")
    suspend fun count(): Int

    @Upsert
    suspend fun upsertAll(items: List<CatalogEntity>)

    @Query("DELETE FROM catalog_items")
    suspend fun clear()
}
