package com.example.androidtemplate.feature.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.androidtemplate.core.model.CatalogItem

@Composable
fun CatalogListRoute(
    onItemClick: (Long) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val pagingItems = viewModel.items.collectAsLazyPagingItems()
    CatalogListScreen(
        items = pagingItems,
        onItemClick = onItemClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogListScreen(
    items: LazyPagingItems<CatalogItem>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    CatalogListLayout(
        itemCount = items.itemCount,
        itemAt = items::get,
        itemKey = items.itemKey(CatalogItem::id),
        itemContentType = items.itemContentType(),
        onItemClick = onItemClick,
        modifier = modifier,
    )
}

@Composable
fun CatalogListScreen(
    items: List<CatalogItem>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    CatalogListLayout(
        itemCount = items.size,
        itemAt = items::get,
        itemKey = { index -> items[index].id },
        itemContentType = { CatalogItem::class },
        onItemClick = onItemClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("LongParameterList")
private fun CatalogListLayout(
    itemCount: Int,
    itemAt: (Int) -> CatalogItem?,
    itemKey: (Int) -> Any,
    itemContentType: (Int) -> Any?,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Catalog") }) },
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 280.dp),
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(count = itemCount, key = itemKey, contentType = itemContentType) { index ->
                val item = itemAt(index)
                if (item == null) {
                    Card(modifier = Modifier.fillMaxWidth().height(88.dp)) {}
                } else {
                    Card(modifier = Modifier.fillMaxWidth().clickable { onItemClick(item.id) }) {
                        ListItem(
                            headlineContent = { Text(item.title) },
                            supportingContent = { Text(item.summary) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CatalogDetailRoute(
    itemId: Long,
    showBackButton: Boolean,
    onBack: () -> Unit,
    viewModel: CatalogDetailViewModel =
        hiltViewModel<CatalogDetailViewModel, CatalogDetailViewModel.Factory>(
            creationCallback = { factory -> factory.create(itemId) },
        ),
) {
    val item by viewModel.item.collectAsStateWithLifecycle()
    CatalogDetailScreen(item = item, showBackButton = showBackButton, onBack = onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogDetailScreen(
    item: CatalogItem?,
    showBackButton: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(item?.title ?: "Item details") },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (item == null) {
                CircularProgressIndicator()
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.semantics { heading() },
                    )
                    Text(item.summary, style = MaterialTheme.typography.bodyLarge)
                    Text("ID ${item.id}", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun CatalogDetailPlaceholder(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Select an item", style = MaterialTheme.typography.titleLarge)
    }
}
