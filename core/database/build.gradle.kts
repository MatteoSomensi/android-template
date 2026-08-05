plugins {
    id("template.android.library")
    id("template.android.hilt")
}

android {
    namespace = "com.example.androidtemplate.core.database"
    testOptions {
        managedDevices.localDevices.create("pixel2Api35") {
            device = "Pixel 2"
            sdkVersion = 35
            systemImageSource = "aosp"
        }
    }
}

ksp { arg("room.schemaLocation", "$projectDir/schemas") }

dependencies {
    implementation(project(":core:model"))
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.room.testing)
}
