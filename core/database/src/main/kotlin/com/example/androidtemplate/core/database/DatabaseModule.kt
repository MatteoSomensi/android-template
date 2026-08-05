package com.example.androidtemplate.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): TemplateDatabase = Room.databaseBuilder(context, TemplateDatabase::class.java, "template.db").build()

    @Provides
    fun provideCatalogDao(database: TemplateDatabase): CatalogDao = database.catalogDao()
}
