plugins {
    id("template.android.library")
    id("template.android.hilt")
}

android { namespace = "com.example.androidtemplate.core.common" }

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.bundles.unit.test)
}
