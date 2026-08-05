package com.example.androidtemplate.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.androidtemplate.core.database.CatalogDao
import com.example.androidtemplate.core.database.CatalogEntity
import com.example.androidtemplate.core.database.TemplateDatabase
import com.example.androidtemplate.core.database.asEntity
import com.example.androidtemplate.core.network.CatalogNetworkDataSource
import kotlinx.coroutines.CancellationException

@OptIn(ExperimentalPagingApi::class)
class CatalogRemoteMediator(
    private val database: TemplateDatabase,
    private val dao: CatalogDao,
    private val network: CatalogNetworkDataSource,
) : RemoteMediator<Int, CatalogEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CatalogEntity>,
    ): MediatorResult =
        @Suppress("TooGenericExceptionCaught")
        try {
            if (loadType == LoadType.PREPEND) return MediatorResult.Success(endOfPaginationReached = true)
            val offset = if (loadType == LoadType.REFRESH) 0 else dao.count()
            val items = network.items(offset = offset, limit = state.config.pageSize)
            database.withTransaction {
                if (loadType == LoadType.REFRESH) dao.clear()
                dao.upsertAll(items.map { it.asEntity() })
            }
            MediatorResult.Success(endOfPaginationReached = items.size < state.config.pageSize)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (error: Exception) {
            MediatorResult.Error(error)
        }
}
