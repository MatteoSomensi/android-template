package com.example.androidtemplate.feature.catalog

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object CatalogList : NavKey

@Serializable
data class CatalogDetail(
    val id: Long,
) : NavKey
