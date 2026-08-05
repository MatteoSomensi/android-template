package com.example.androidtemplate.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.androidtemplate.core.database.CatalogDao
import com.example.androidtemplate.core.database.TemplateDatabase
import com.example.androidtemplate.core.database.asExternalModel
import com.example.androidtemplate.core.domain.CatalogRepository
import com.example.androidtemplate.core.model.CatalogItem
import com.example.androidtemplate.core.network.CatalogNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFirstCatalogRepository
    @Inject
    constructor(
        database: TemplateDatabase,
        private val dao: CatalogDao,
        network: CatalogNetworkDataSource,
    ) : CatalogRepository {
        @OptIn(ExperimentalPagingApi::class)
        private val pager =
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = CatalogRemoteMediator(database, dao, network),
                pagingSourceFactory = dao::pagingSource,
            )

        override fun pagedItems(): Flow<PagingData<CatalogItem>> = pager.flow.map { pagingData -> pagingData.map { it.asExternalModel() } }

        override fun observeItem(id: Long): Flow<CatalogItem?> = dao.observe(id).map { it?.asExternalModel() }

        override suspend fun refresh() {
            // Paging owns refresh orchestration. Product code can add an explicit invalidation policy here.
            dao.pagingSource().invalidate()
        }
    }
