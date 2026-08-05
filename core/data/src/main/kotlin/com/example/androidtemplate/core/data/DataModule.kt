package com.example.androidtemplate.core.data

import com.example.androidtemplate.core.domain.CatalogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindCatalogRepository(implementation: OfflineFirstCatalogRepository): CatalogRepository
}
