plugins { alias(libs.plugins.kotlin.jvm) }

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    api(project(":core:model"))
    api(project(":core:domain"))
    api(libs.kotlinx.coroutines.test)
    api(libs.paging.common)
}
