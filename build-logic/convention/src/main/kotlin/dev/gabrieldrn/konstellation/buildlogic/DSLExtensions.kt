package dev.gabrieldrn.konstellation.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private const val STRICT_API_COMPILER_ARG = "-Xexplicit-api=strict"

/**
 * Shortcut to configure Kotlin compiler options.
 */
internal fun CommonExtension<*, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}

/**
 * Setup explicit api for all kotlin compile tasks except tests.
 * @see <a href="https://github.com/Kotlin/KEEP/blob/master/proposals/explicit-api-mode.md">Explicit API mode</a>
 */
internal fun Project.setupExplicitApi() {
    tasks
        .matching {
            it is KotlinCompile && !it.name.contains("test", ignoreCase = true)
        }
        .matching {
            !(it as KotlinCompile).kotlinOptions.freeCompilerArgs.contains(STRICT_API_COMPILER_ARG)
        }
        .configureEach {
            (this as KotlinCompile).kotlinOptions.freeCompilerArgs += STRICT_API_COMPILER_ARG
        }
}
