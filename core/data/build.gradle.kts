plugins {
    id("template.android.library")
    id("template.android.hilt")
}

android { namespace = "com.example.androidtemplate.core.data" }

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(libs.paging.runtime)
    implementation(libs.room.ktx)
    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)
}
