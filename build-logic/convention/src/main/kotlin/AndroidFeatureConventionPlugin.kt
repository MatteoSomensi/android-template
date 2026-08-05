import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("template.android.library")
        pluginManager.apply("template.android.compose")
        pluginManager.apply("template.android.hilt")
    }
}
