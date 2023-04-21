import com.gabrieldrn.konstellation.buildlogic.getPlugin
import com.gabrieldrn.konstellation.buildlogic.javaVersion
import com.gabrieldrn.konstellation.buildlogic.libs
import com.gabrieldrn.konstellation.buildlogic.setupExplicitApi
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

class KonstellationJavaLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        val libs = libs
        with(pluginManager) {
            apply("java-library")
            apply(libs.getPlugin("kotlin-jvm"))
        }

        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        setupExplicitApi()
    }
}