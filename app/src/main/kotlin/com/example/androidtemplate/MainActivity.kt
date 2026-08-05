package com.example.androidtemplate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import com.example.androidtemplate.core.designsystem.TemplateAppTheme
import com.example.androidtemplate.core.navigation.EntryProviderInstaller
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var entryInstallers: Set<@JvmSuppressWildcards EntryProviderInstaller>

    private val deepLink = mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deepLink.value = intent?.data
        enableEdgeToEdge()
        setContent {
            TemplateAppTheme {
                TemplateNavigation(
                    entryInstallers = entryInstallers,
                    deepLink = deepLink.value,
                    onDeepLinkConsumed = { deepLink.value = null },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        deepLink.value = intent.data
    }
}
