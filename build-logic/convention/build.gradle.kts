import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins { `kotlin-dsl` }

group = "com.example.androidtemplate.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    implementation(libs.roborazzi.gradle.plugin)
    testImplementation(gradleTestKit())
    testImplementation(libs.junit)
}

gradlePlugin {
    plugins {
        register("androidApplication") { id = "template.android.application"; implementationClass = "AndroidApplicationConventionPlugin" }
        register("androidLibrary") { id = "template.android.library"; implementationClass = "AndroidLibraryConventionPlugin" }
        register("androidCompose") { id = "template.android.compose"; implementationClass = "AndroidComposeConventionPlugin" }
        register("androidHilt") { id = "template.android.hilt"; implementationClass = "AndroidHiltConventionPlugin" }
        register("androidFeature") { id = "template.android.feature"; implementationClass = "AndroidFeatureConventionPlugin" }
        register("androidRoborazzi") { id = "template.android.roborazzi"; implementationClass = "AndroidRoborazziConventionPlugin" }
        register("templateBootstrap") { id = "template.bootstrap"; implementationClass = "TemplateBootstrapPlugin" }
    }
}
