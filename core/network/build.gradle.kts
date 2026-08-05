plugins {
    id("template.android.library")
    id("template.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android { namespace = "com.example.androidtemplate.core.network" }

dependencies {
    implementation(project(":core:model"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
}
