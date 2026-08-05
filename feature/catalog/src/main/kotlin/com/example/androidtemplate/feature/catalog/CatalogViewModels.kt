package com.example.androidtemplate.feature.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.androidtemplate.core.domain.CatalogRepository
import com.example.androidtemplate.core.model.CatalogItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel
    @Inject
    constructor(
        repository: CatalogRepository,
    ) : ViewModel() {
        val items = repository.pagedItems().cachedIn(viewModelScope)
    }

@HiltViewModel(assistedFactory = CatalogDetailViewModel.Factory::class)
class CatalogDetailViewModel
    @AssistedInject
    constructor(
        repository: CatalogRepository,
        @Assisted itemId: Long,
    ) : ViewModel() {
        val item: StateFlow<CatalogItem?> =
            repository.observeItem(itemId).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null,
            )

        @AssistedFactory
        interface Factory {
            fun create(itemId: Long): CatalogDetailViewModel
        }
    }
