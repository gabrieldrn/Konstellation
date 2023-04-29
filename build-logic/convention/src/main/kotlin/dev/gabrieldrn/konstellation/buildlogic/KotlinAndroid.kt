package dev.gabrieldrn.konstellation.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

/**
 * Configure base Kotlin with Android options.
 */
internal fun Project.configureKotlinAndroidCommon(
    commonExtension: CommonExtension<*, *, *, *>,
) = with(commonExtension) {
    compileSdk = libs.getVersion("targetSdk").toInt()
    buildToolsVersion = libs.getVersion("buildTools")

    defaultConfig {
        minSdk = libs.getVersion("minSdk").toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = dev.gabrieldrn.konstellation.buildlogic.javaVersion
        targetCompatibility = dev.gabrieldrn.konstellation.buildlogic.javaVersion
    }

    kotlinOptions {
        jvmTarget = dev.gabrieldrn.konstellation.buildlogic.javaVersion.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.getVersion("androidxComposeCompiler")
    }
}
