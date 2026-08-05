plugins {
    id("template.android.library")
    id("template.android.compose")
}

android { namespace = "com.example.androidtemplate.platform.widget" }

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
}
