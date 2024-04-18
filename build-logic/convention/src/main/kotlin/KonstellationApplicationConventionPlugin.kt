import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import dev.gabrieldrn.konstellation.buildlogic.configureKotlinAndroidCommon
import dev.gabrieldrn.konstellation.buildlogic.getPlugin
import dev.gabrieldrn.konstellation.buildlogic.getVersion
import dev.gabrieldrn.konstellation.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KonstellationApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        val libs = libs

        with(pluginManager) {
            apply(libs.getPlugin("android-application"))
            apply(libs.getPlugin("kotlin-android"))
        }

        extensions.configure<BaseAppModuleExtension> {

            configureKotlinAndroidCommon(this)

            defaultConfig {
                targetSdk = libs.getVersion("targetSdk").toInt()
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                }
                debug {
                    isDebuggable = true
                }
            }
        }
    }
}
