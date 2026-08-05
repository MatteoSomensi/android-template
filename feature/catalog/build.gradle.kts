plugins {
    id("template.android.feature")
    id("template.android.roborazzi")
    alias(libs.plugins.kotlin.serialization)
}

android { namespace = "com.example.androidtemplate.feature.catalog" }

roborazzi { outputDir.set(layout.projectDirectory.dir("src/test/screenshots")) }

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.paging.compose)
    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.robolectric)
}
