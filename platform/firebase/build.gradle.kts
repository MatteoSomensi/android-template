plugins { id("template.android.library") }

android { namespace = "com.example.androidtemplate.platform.firebase" }

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.config)
}
