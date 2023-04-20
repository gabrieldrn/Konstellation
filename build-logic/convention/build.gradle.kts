@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.gabrieldrn.konstellation.build-logic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "konstellation.android.library"
            implementationClass = "KonstellationAndroidLibraryConventionPlugin"
        }

        // TODO Android application plugin
        // TODO Publication plugin
        // TODO Dokka plugin
    }
}
