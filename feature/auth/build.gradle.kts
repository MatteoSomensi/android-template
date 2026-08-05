plugins { id("template.android.feature") }

android { namespace = "com.example.androidtemplate.feature.auth" }

dependencies {
    implementation(project(":platform:firebase"))
    implementation(project(":core:designsystem"))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.google.id)
}
