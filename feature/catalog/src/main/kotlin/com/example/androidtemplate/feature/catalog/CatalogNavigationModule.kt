package com.example.androidtemplate.feature.catalog

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.LocalListDetailSceneScope
import com.example.androidtemplate.core.navigation.EntryProviderInstaller
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoSet

private data object CatalogListDetailScene

@Module
@InstallIn(ActivityComponent::class)
object CatalogNavigationModule {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @Provides
    @IntoSet
    fun provideCatalogEntries(): EntryProviderInstaller =
        { navigator ->
            entry<CatalogList>(
                metadata =
                    ListDetailSceneStrategy.listPane(
                        sceneKey = CatalogListDetailScene,
                        detailPlaceholder = { CatalogDetailPlaceholder() },
                    ),
            ) {
                CatalogListRoute(onItemClick = { navigator.navigate(CatalogDetail(it)) })
            }
            entry<CatalogDetail>(
                metadata = ListDetailSceneStrategy.detailPane(sceneKey = CatalogListDetailScene),
            ) { key ->
                CatalogDetailRoute(
                    itemId = key.id,
                    showBackButton = LocalListDetailSceneScope.current == null,
                    onBack = navigator::goBack,
                )
            }
        }
}
