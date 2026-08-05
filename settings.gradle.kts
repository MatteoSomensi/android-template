pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "AndroidTemplate"

include(":app")
include(":core:common")
include(":core:model")
include(":core:domain")
include(":core:designsystem")
include(":core:navigation")
include(":core:network")
include(":core:database")
include(":core:data")
include(":core:testing")
include(":feature:catalog")

// TEMPLATE_OPTIONAL_MODULES_START
include(":platform:firebase")
include(":platform:sync")
include(":platform:widget")
include(":platform:appfunctions")
include(":feature:auth")
include(":macrobenchmark")
// TEMPLATE_OPTIONAL_MODULES_END
