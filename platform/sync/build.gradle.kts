plugins {
    id("template.android.library")
    id("template.android.hilt")
}

android { namespace = "com.example.androidtemplate.platform.sync" }

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.work.runtime)
    implementation(libs.androidx.hilt.work)
    ksp(libs.hilt.compiler)
    testImplementation(libs.work.testing)
    testImplementation(libs.junit)
}
