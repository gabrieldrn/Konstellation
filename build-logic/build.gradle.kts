plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.gabrieldrn.konstellation"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

@Suppress("UseTomlInstead") // Unavailable here
dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
    compileOnly("com.android.tools.build:gradle:8.0.0")
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
