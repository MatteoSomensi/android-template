plugins {
    id("template.android.application")
    id("template.android.compose")
    id("template.android.hilt")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidx.baselineprofile)
    alias(libs.plugins.cyclonedx)
}

android {
    namespace = "com.example.androidtemplate"
    defaultConfig {
        applicationId = "com.example.androidtemplate"
        versionCode = providers.gradleProperty("VERSION_CODE").orNull?.toIntOrNull() ?: 1
        versionName = providers.gradleProperty("VERSION_NAME").orNull ?: "0.1.0"
    }
    buildTypes {
        create("benchmark") {
            initWith(getByName("release"))
            matchingFallbacks += "release"
            isDebuggable = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    testOptions {
        managedDevices.localDevices.create("pixel2Api35") {
            device = "Pixel 2"
            sdkVersion = 35
            systemImageSource = "aosp"
        }
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:data"))
    implementation(project(":feature:catalog"))
    // TEMPLATE_OPTIONAL_DEPENDENCIES_START
    implementation(project(":platform:sync"))
    implementation(project(":platform:widget"))
    implementation(project(":platform:appfunctions"))
    implementation(project(":feature:auth"))
    // TEMPLATE_OPTIONAL_DEPENDENCIES_END
    implementation(libs.bundles.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.adaptive.navigation3)
    implementation(libs.androidx.adaptive.navigation.suite)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":macrobenchmark"))

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
