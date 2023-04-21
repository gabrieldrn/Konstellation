@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.gabrieldrn.konstellation.buildlogic"

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

        register("javaLibrary") {
            id = "konstellation.java.library"
            implementationClass = "KonstellationJavaLibraryConventionPlugin"
        }

        register("application") {
            id = "konstellation.android.application"
            implementationClass = "KonstellationApplicationConventionPlugin"
        }

        // TODO Publication plugin
        // TODO Dokka plugin
    }
}
