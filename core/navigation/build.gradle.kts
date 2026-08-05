plugins {
    id("template.android.library")
    id("template.android.compose")
    id("template.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android { namespace = "com.example.androidtemplate.core.navigation" }

dependencies {
    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.compose)
}
