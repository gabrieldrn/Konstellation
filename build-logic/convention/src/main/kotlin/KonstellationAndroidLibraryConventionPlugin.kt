import com.android.build.gradle.LibraryExtension
import dev.gabrieldrn.konstellation.buildlogic.configureKotlinAndroidCommon
import dev.gabrieldrn.konstellation.buildlogic.getLibrary
import dev.gabrieldrn.konstellation.buildlogic.getPlugin
import dev.gabrieldrn.konstellation.buildlogic.libs
import dev.gabrieldrn.konstellation.buildlogic.setupExplicitApi
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * A plugin used by android libraries modules from Konstellation to configure themselves. It
 * provides a convention to keep consistency across those modules.
 */
class KonstellationAndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        val libs = libs

        with(pluginManager) {
            apply(libs.getPlugin("android-library"))
            apply(libs.getPlugin("kotlin-android"))
        }

        extensions.configure<LibraryExtension> {

            configureKotlinAndroidCommon(this)

            setupExplicitApi()

            // TODO Not sure if proguard/R8 is relevant for libraries.

            defaultConfig {
                consumerProguardFiles.add(file("consumer-rules.pro"))
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            testOptions {
                unitTests.all {
                    it.testLogging {
                        events = setOf(
                            TestLogEvent.PASSED,
                            TestLogEvent.SKIPPED,
                            TestLogEvent.FAILED,
//                            TestLogEvent.STANDARD_OUT,
//                            TestLogEvent.STANDARD_ERROR
                        )
                        exceptionFormat = TestExceptionFormat.FULL
                        showStandardStreams = true
                        showExceptions = true
                        showCauses = true
                        showStackTraces = true
                        showStandardStreams = true
                    }
                }
            }
        }

        dependencies {
            add("testImplementation", libs.getLibrary("junit"))
            add("testImplementation", libs.getLibrary("kotlin-test-junit"))
            add("androidTestImplementation", libs.getLibrary("androidx-test-ext"))
            add("androidTestImplementation", libs.getLibrary("androidx-test-espresso"))
        }
    }
}
