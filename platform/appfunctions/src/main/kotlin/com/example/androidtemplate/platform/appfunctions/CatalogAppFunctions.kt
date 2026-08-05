package com.example.androidtemplate.platform.appfunctions

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.service.AppFunction
import com.example.androidtemplate.core.domain.CatalogRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CatalogAppFunctions
    @Inject
    constructor(
        private val repository: CatalogRepository,
    ) {
        /** Returns the title for a positive catalog identifier, or null when it is unavailable. */
        @AppFunction(isDescribedByKDoc = true)
        @Suppress("UnusedParameter")
        suspend fun catalogTitle(
            appFunctionContext: AppFunctionContext,
            itemId: Long,
        ): String? = repository.observeItem(itemId).first()?.title
    }
