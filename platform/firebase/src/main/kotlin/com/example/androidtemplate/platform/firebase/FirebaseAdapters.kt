package com.example.androidtemplate.platform.firebase

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

data class FirebaseAdapters(
    val auth: FirebaseAuth,
    val analytics: FirebaseAnalytics,
    val crashlytics: FirebaseCrashlytics,
    val remoteConfig: FirebaseRemoteConfig,
)
