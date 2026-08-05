import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File

private const val TEMPLATE_PACKAGE = "com.example.androidtemplate"
private const val TEMPLATE_PACKAGE_PATH = "com/example/androidtemplate"
private const val MAX_BOOTSTRAP_TEXT_FILE_BYTES = 10L * 1024 * 1024

class TemplateBootstrapPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("bootstrapTemplate", BootstrapTemplateTask::class.java) {
            group = "template"
            description = "Renames this template and removes unselected optional capabilities."
            notCompatibleWithConfigurationCache("This one-shot task intentionally rewrites its own build.")
        }
    }
}

open class BootstrapTemplateTask : DefaultTask() {
    @TaskAction
    fun bootstrap() {
        val root = project.rootDir
        val appName = value("template.appName", "App name", "Sample App")
        val packageName = value("template.packageName", "Package name", "com.example.sample")
        val repository = value("template.repository", "GitHub repository", "owner/repository")
        val deepLinkScheme = value("template.deepLinkScheme", "Deep-link scheme", packageName.substringAfterLast('.'))
        val requestedPreset = project.findProperty("template.preset")?.toString()?.ifBlank { null } ?: "standard"
        val requestedFeatures = project.findProperty("template.features")?.toString()?.ifBlank { null }
        val dryRun = project.findProperty("template.dryRun")?.toString()?.toBooleanStrictOrNull() ?: false

        require(packageName.matches(Regex("[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)+"))) {
            "template.packageName must be a lowercase dotted JVM package."
        }
        require(repository.matches(Regex("[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+"))) {
            "template.repository must use owner/repository format."
        }
        require(deepLinkScheme.matches(Regex("[a-z][a-z0-9+.-]*"))) {
            "template.deepLinkScheme must be a valid lowercase URI scheme."
        }
        ensureCleanWorkingTree(root)

        val capabilities = resolveCapabilities(requestedPreset, requestedFeatures)
        val projectName = appName.toPascalIdentifier()
        val repoName = repository.substringAfter('/')
        val replacements =
            linkedMapOf(
                TEMPLATE_PACKAGE to packageName,
                TEMPLATE_PACKAGE_PATH to packageName.replace('.', '/'),
                "AndroidTemplate" to projectName,
                "Android Template" to appName,
                "TemplateAppTheme" to "${projectName}Theme",
                "MatteoSomensi/android-template" to repository,
                "android-template" to repoName,
                "starter://catalog" to "$deepLinkScheme://catalog",
                "android:scheme=\"starter\"" to "android:scheme=\"$deepLinkScheme\"",
                "scheme != \"starter\"" to "scheme != \"$deepLinkScheme\"",
            )

        logger.lifecycle("Template bootstrap${if (dryRun) " dry-run" else ""}")
        logger.lifecycle("  appName=$appName")
        logger.lifecycle("  packageName=$packageName")
        logger.lifecycle("  repository=$repository")
        logger.lifecycle("  features=${capabilities.sorted().joinToString()}")
        if (dryRun) return

        rewriteTextFiles(root, replacements)
        movePackageDirectories(root, packageName.replace('.', '/'))
        pruneCapabilities(root, capabilities)
        removeBootstrapInfrastructure(root)
        removeInternalMarkers(root)
        val staleTokens = listOf(TEMPLATE_PACKAGE, "bootstrapTemplate", "template.appName", "TEMPLATE_")
        val staleFile =
            root.walkTopDown()
                .onEnter { it.name !in setOf(".git", ".gradle", "build") }
                .filter { it.isFile && it.extension in textExtensions }
                .firstOrNull { file -> staleTokens.any(file.readBootstrapText()::contains) }
        check(staleFile == null) { "Bootstrap left internal template references in ${staleFile?.relativeTo(root)}." }
        logger.lifecycle("Bootstrap complete. Run ./gradlew qualityGate before committing.")
    }

    private fun value(property: String, prompt: String, default: String): String {
        project.findProperty(property)?.toString()?.takeIf(String::isNotBlank)?.let { return it }
        check(!System.getenv("CI").equals("true", ignoreCase = true)) {
            "$property is required in CI. Pass it with -P$property=<value>."
        }
        val console = System.console()
        val answer =
            if (console != null) {
                console.readLine("$prompt [$default]: ")
            } else {
                logger.lifecycle("$prompt [$default]: ")
                readlnOrNull()
            }
        return answer?.trim().orEmpty().ifBlank { default }
    }
}

internal fun resolveCapabilities(preset: String, explicit: String?): Set<String> {
    val known = setOf("firebase", "auth", "sync", "widget", "appfunctions", "benchmark", "roborazzi")
    val selected =
        explicit?.split(',')?.map(String::trim)?.filter(String::isNotEmpty)?.toSet()
            ?: when (preset) {
                "minimal" -> emptySet()
                "standard" -> setOf("sync", "benchmark", "roborazzi")
                "full" -> known
                else -> error("Unknown template.preset '$preset'. Use minimal, standard, full, or template.features.")
            }
    require(selected.all(known::contains)) { "Unknown capabilities: ${(selected - known).sorted()}" }
    require("auth" !in selected || "firebase" in selected) { "auth requires firebase." }
    return selected
}

internal fun String.toPascalIdentifier(): String =
    split(Regex("[^A-Za-z0-9]+"))
        .filter(String::isNotBlank)
        .joinToString("") { part -> part.lowercase().replaceFirstChar(Char::uppercase) }
        .ifBlank { error("App name must contain at least one letter or number.") }
        .let { if (it.first().isDigit()) "App$it" else it }

private fun ensureCleanWorkingTree(root: File) {
    if (!File(root, ".git").isDirectory) return
    val process = ProcessBuilder("git", "status", "--porcelain").directory(root).start()
    val output = process.inputStream.bufferedReader().readText()
    check(process.waitFor() == 0 && output.isBlank()) {
        "Bootstrap requires a clean working tree so all changes remain reviewable."
    }
}

private val textExtensions = setOf("kt", "kts", "xml", "md", "yml", "yaml", "toml", "properties", "json", "txt")

private fun rewriteTextFiles(root: File, replacements: Map<String, String>) {
    root.walkTopDown()
        .onEnter { it.name !in setOf(".git", ".gradle", "build") }
        .filter { it.isFile && it.extension in textExtensions }
        .forEach { file ->
            val original = file.readBootstrapText()
            val rewritten = replaceTokens(original, replacements)
            if (rewritten != original) file.writeText(rewritten)
        }
}

internal fun replaceTokens(text: String, replacements: Map<String, String>): String {
    if (replacements.isEmpty()) return text
    val pattern =
        replacements.keys
            .sortedByDescending(String::length)
            .joinToString("|") { Regex.escape(it) }
            .toRegex()
    return pattern.replace(text) { match -> replacements.getValue(match.value) }
}

private fun movePackageDirectories(root: File, targetPath: String) {
    root.walkTopDown()
        .onEnter { it.name !in setOf(".git", ".gradle", "build") }
        .filter { it.isDirectory && it.invariantSeparatorsPath.endsWith(TEMPLATE_PACKAGE_PATH) }
        .toList()
        .forEach { source ->
            val javaRoot = source.invariantSeparatorsPath.removeSuffix(TEMPLATE_PACKAGE_PATH)
            val target = File(javaRoot, targetPath)
            target.parentFile.mkdirs()
            check(source.copyRecursively(target, overwrite = false)) { "Could not move package directory $source" }
            source.deleteRecursively()
            generateSequence(source.parentFile) { it.parentFile }
                .takeWhile { it != File(javaRoot) }
                .filter { it.isDirectory && it.list().isNullOrEmpty() }
                .forEach(File::delete)
        }
}

private data class Capability(val name: String, val projectPath: String, val directory: String)

private val optionalCapabilities =
    listOf(
        Capability("firebase", ":platform:firebase", "platform/firebase"),
        Capability("auth", ":feature:auth", "feature/auth"),
        Capability("sync", ":platform:sync", "platform/sync"),
        Capability("widget", ":platform:widget", "platform/widget"),
        Capability("appfunctions", ":platform:appfunctions", "platform/appfunctions"),
        Capability("benchmark", ":macrobenchmark", "macrobenchmark"),
    )

private fun pruneCapabilities(root: File, selected: Set<String>) {
    val settings = File(root, "settings.gradle.kts")
    var settingsText = settings.readText()
    optionalCapabilities.filterNot { it.name in selected }.forEach { capability ->
        File(root, capability.directory).deleteRecursively()
        settingsText = settingsText.replace("include(\"${capability.projectPath}\")\n", "")
        val appBuild = File(root, "app/build.gradle.kts")
        appBuild.writeText(
            appBuild.readText().replace("    implementation(project(\"${capability.projectPath}\"))\n", ""),
        )
    }
    settings.writeText(settingsText)

    if ("roborazzi" !in selected) {
        val featureBuild = File(root, "feature/catalog/build.gradle.kts")
        featureBuild.writeText(
            featureBuild.readText()
                .replace("    id(\"template.android.roborazzi\")\n", "")
                .replace(Regex("(?s)\\nroborazzi \\{.*?\\}\n"), "\n"),
        )
        File(root, "feature/catalog/src/test/screenshots").deleteRecursively()
        File(root, "feature/catalog/src/test")
            .walkTopDown()
            .firstOrNull { it.isFile && it.name == "CatalogScreenshotTest.kt" }
            ?.delete()
    }
    if ("benchmark" !in selected) {
        val appBuild = File(root, "app/build.gradle.kts")
        appBuild.writeText(appBuild.readText().replace("    \"baselineProfile\"(project(\":macrobenchmark\"))\n", ""))
    }
}

private fun removeBootstrapInfrastructure(root: File) {
    val rootBuild = File(root, "build.gradle.kts")
    rootBuild.writeText(rootBuild.readText().replace("    id(\"template.bootstrap\")\n", ""))
    val conventionBuild = File(root, "build-logic/convention/build.gradle.kts")
    conventionBuild.writeText(
        conventionBuild.readText().replace(
            "        register(\"templateBootstrap\") { id = \"template.bootstrap\"; implementationClass = \"TemplateBootstrapPlugin\" }\n",
            "",
        ),
    )
    File(root, "build-logic/convention/src/main/kotlin/TemplateBootstrapPlugin.kt").delete()
    File(root, "build-logic/convention/src/test/kotlin/TemplateBootstrapPluginTest.kt").delete()
    File(root, ".github/workflows/template-smoke.yml").delete()
    val readme = File(root, "README.md")
    if (readme.isFile) {
        readme.writeText(
            readme.readBootstrapText().replace(
                Regex("(?s)<!-- TEMPLATE_BOOTSTRAP_START -->.*?<!-- TEMPLATE_BOOTSTRAP_END -->\\n*"),
                "",
            ),
        )
    }
}

private fun removeInternalMarkers(root: File) {
    listOf(File(root, "settings.gradle.kts"), File(root, "app/build.gradle.kts"))
        .filter(File::isFile)
        .forEach { file ->
            val original = file.readBootstrapText()
            val cleaned = original.lineSequence().filterNot { "TEMPLATE_OPTIONAL_" in it }.joinToString("\n")
            file.writeText(cleaned)
        }
}

private fun File.readBootstrapText(): String {
    check(length() <= MAX_BOOTSTRAP_TEXT_FILE_BYTES) {
        "Bootstrap cannot safely rewrite ${invariantSeparatorsPath}: text file exceeds 10 MiB."
    }
    return readText()
}
