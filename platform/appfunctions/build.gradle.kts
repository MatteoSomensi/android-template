plugins {
    id("template.android.library")
    id("template.android.hilt")
}

android { namespace = "com.example.androidtemplate.platform.appfunctions" }

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.androidx.appfunctions)
    implementation(libs.androidx.appfunctions.service)
    ksp(libs.androidx.appfunctions.compiler)
}
