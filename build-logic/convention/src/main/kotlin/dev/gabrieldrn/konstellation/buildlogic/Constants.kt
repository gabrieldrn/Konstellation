package dev.gabrieldrn.konstellation.buildlogic

/**
 * Global constants for the build logic.
 */
@Suppress("UndocumentedPublicProperty")
object Constants {
    const val GROUP_ID = "dev.gabrieldrn.konstellation"
    const val MAVEN_REPO_NAME = "GitHub"
    const val MAVEN_REPO_URL =
        "https://maven.pkg.github.com/gabrieldrn/Konstellation"
    const val MAVEN_REPO_USERNAME_ENV_KEY = "GPR_USER"
    const val MAVEN_REPO_PASSWORD_ENV_KEY = "GPR_KEY"
    const val MAVEN_REPO_USERNAME_LOCAL_KEY = "GITHUB_USER"
    const val MAVEN_REPO_PASSWORD_LOCAL_KEY = "GITHUB_PERSONAL_ACCESS_TOKEN"
}
