package dev.gabrieldrn.konstellation.buildlogic

import org.gradle.api.JavaVersion

internal val JAVA_VERSION = JavaVersion.VERSION_17
internal const val STRICT_API_COMPILER_ARG = "-Xexplicit-api=strict"

internal const val GROUP_ID = "dev.gabrieldrn.konstellation"

internal const val MAVEN_REPO_NAME = "GitHubPackages"
internal const val MAVEN_REPO_URL = "https://maven.pkg.github.com/gabrieldrn/Konstellation"
internal const val MAVEN_REPO_USERNAME_ENV_KEY = "gpr.user"
internal const val MAVEN_REPO_PASSWORD_ENV_KEY = "gpr.key"
