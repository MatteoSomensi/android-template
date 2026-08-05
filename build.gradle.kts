import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.androidx.baselineprofile) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.cyclonedx) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    id("template.bootstrap")
}

detekt {
    config.setFrom(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
}

abstract class VerifyModuleBoundariesTask : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val moduleBuildFiles: ConfigurableFileCollection

    @TaskAction
    fun verify() {
        val violations = mutableListOf<String>()
        moduleBuildFiles.files.forEach { buildFile ->
            val path = buildFile.invariantSeparatorsPath
            val dependencies =
                Regex("""project\(\"(:[^\"]+)\"\)""")
                    .findAll(buildFile.readText())
                    .map { it.groupValues[1] }
                    .toSet()

            fun forbid(vararg prefixes: String) {
                dependencies
                    .filter { dependency -> prefixes.any(dependency::startsWith) }
                    .forEach { violations += "$path must not depend on $it" }
            }

            when {
                path.endsWith("/core/model/build.gradle.kts") -> forbid(":app", ":core:", ":feature:", ":platform:")
                path.endsWith(
                    "/core/domain/build.gradle.kts",
                ) -> forbid(":app", ":core:data", ":core:database", ":core:network", ":core:designsystem", ":feature:", ":platform:")
                path.endsWith(
                    "/core/designsystem/build.gradle.kts",
                ) -> forbid(":app", ":core:data", ":core:database", ":core:domain", ":core:network", ":feature:", ":platform:")
                path.contains("/feature/") -> forbid(":core:data", ":core:database", ":core:network")
            }
        }
        check(violations.isEmpty()) { "Module boundary violations:\n${violations.joinToString("\n")}" }
    }
}

tasks.register<VerifyModuleBoundariesTask>("verifyModuleBoundaries") {
    group = "verification"
    moduleBuildFiles.from(
        fileTree(layout.projectDirectory) {
            include("**/build.gradle.kts")
            exclude("build-logic/**")
            exclude("**/build/**")
            exclude(".gradle/**")
        },
    )
}

val qualityGate =
    tasks.register("qualityGate") {
        group = "verification"
        description = "Runs the deterministic local verification suite."
        dependsOn("verifyModuleBoundaries", "ktlintCheck", "detekt")
    }

subprojects {
    pluginManager.apply("io.gitlab.arturbosch.detekt")
    pluginManager.apply("org.jlleitschuh.gradle.ktlint")
    pluginManager.apply("jacoco")

    extensions.configure<DetektExtension> {
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
        parallel = true
    }

    qualityGate.configure {
        dependsOn(
            tasks.matching {
                it.name in setOf("testDebugUnitTest", "lintDebug", "assembleDebug")
            },
        )
    }
}
