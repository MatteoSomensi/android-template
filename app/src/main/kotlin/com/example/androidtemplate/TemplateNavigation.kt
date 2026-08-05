package com.example.androidtemplate

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.androidtemplate.core.navigation.EntryProviderInstaller
import com.example.androidtemplate.core.navigation.Navigator
import com.example.androidtemplate.feature.catalog.CatalogDetail
import com.example.androidtemplate.feature.catalog.CatalogList

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TemplateNavigation(
    entryInstallers: Set<EntryProviderInstaller>,
    deepLink: Uri?,
    onDeepLinkConsumed: () -> Unit,
) {
    val backStack = rememberNavBackStack(CatalogList)
    val navigator = remember(backStack) { Navigator(backStack) }
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val directive =
        remember(adaptiveInfo) {
            calculatePaneScaffoldDirective(adaptiveInfo).copy(horizontalPartitionSpacerSize = 0.dp)
        }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

    LaunchedEffect(deepLink) {
        deepLink?.toCatalogItemId()?.let { navigator.replaceAll(CatalogList, CatalogDetail(it)) }
        if (deepLink != null) onDeepLinkConsumed()
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                selected = true,
                onClick = { navigator.replaceAll(CatalogList) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Catalog") },
                label = { Text("Catalog") },
            )
        },
        layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo),
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { navigator.goBack() },
            sceneStrategies = listOf(listDetailStrategy),
            entryProvider =
                entryProvider {
                    entryInstallers.forEach { installer -> installer(navigator) }
                },
        )
    }
}

internal fun Uri.toCatalogItemId(): Long? {
    if (scheme != "starter" || host != "catalog") return null
    return lastPathSegment?.toLongOrNull()?.takeIf { it > 0 }
}
