package com.example.androidtemplate.platform.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text

class CatalogWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        provideContent { CatalogWidgetContent() }
    }
}

@Composable
private fun CatalogWidgetContent() {
    GlanceTheme {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Catalog")
            Text("Replace this widget with product content")
        }
    }
}

class CatalogWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = CatalogWidget()
}
