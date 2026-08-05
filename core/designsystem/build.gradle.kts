plugins {
    id("template.android.library")
    id("template.android.compose")
}

android { namespace = "com.example.androidtemplate.core.designsystem" }

dependencies { implementation(libs.bundles.compose) }
