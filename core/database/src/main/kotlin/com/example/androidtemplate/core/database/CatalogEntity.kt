package com.example.androidtemplate.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidtemplate.core.model.CatalogItem

@Entity(tableName = "catalog_items")
data class CatalogEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val summary: String,
)

fun CatalogEntity.asExternalModel() = CatalogItem(id, title, summary)

fun CatalogItem.asEntity() = CatalogEntity(id, title, summary)
