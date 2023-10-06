import dev.gabrieldrn.konstellation.buildlogic.JAVA_VERSION
import dev.gabrieldrn.konstellation.buildlogic.getPlugin
import dev.gabrieldrn.konstellation.buildlogic.libs
import dev.gabrieldrn.konstellation.buildlogic.setupExplicitApi
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

/**
 * A plugin used by java libraries modules from Konstellation to configure themselves. It provides
 * a convention to keep consistency across those modules.
 */
class KonstellationJavaLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        val libs = libs
        with(pluginManager) {
            apply("java-library")
            apply(libs.getPlugin("kotlin-jvm"))
        }

        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }

        setupExplicitApi()

        // TODO Configure test tasks.
    }
}
