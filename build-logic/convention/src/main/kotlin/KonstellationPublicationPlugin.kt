import dev.gabrieldrn.konstellation.buildlogic.Constants
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import java.net.URI

class KonstellationPublicationPlugin : Plugin<Project> {

    private fun Project.publishing(config: PublishingExtension.() -> Unit) =
        extensions.configure(config)

    override fun apply(target: Project) = with(target) {

        val version = (properties["VERSION"] ?: error("Project version is unspecified.")) as String

        val mavenRepoUsername = System.getenv(Constants.MAVEN_REPO_USERNAME_ENV_KEY)
            ?: properties[Constants.MAVEN_REPO_USERNAME_LOCAL_KEY] as String?

        val artifactId = if (group.toString().contains("charts")) {
            "${name}-chart"
        } else {
            name
        }

        require(!mavenRepoUsername.isNullOrEmpty()) {
            "Maven publication: Unable to retrieve the username for the credentials repository." +
                    "Either '${Constants.MAVEN_REPO_USERNAME_ENV_KEY}' env variable " +
                    "or '${Constants.MAVEN_REPO_USERNAME_LOCAL_KEY}' local property is not set."
        }

        val mavenRepoPassword = System.getenv(Constants.MAVEN_REPO_PASSWORD_ENV_KEY)
            ?: properties[Constants.MAVEN_REPO_PASSWORD_LOCAL_KEY] as String?

        require(!mavenRepoPassword.isNullOrEmpty()) {
            "Maven publication: Unable to retrieve the password for the credentials repository." +
                    "Either '${Constants.MAVEN_REPO_PASSWORD_ENV_KEY}' env variable " +
                    "or '${Constants.MAVEN_REPO_PASSWORD_LOCAL_KEY}' local property is not set."
        }

        pluginManager.apply("org.gradle.maven-publish")

        fun MavenPublication.configurePom() {
            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                configurations
                    .asSequence()
                    .filterNot {
                        it.name.contains("test", ignoreCase = true)
                    }
//                    .onEach { println(it.name) }
                    .map { it.dependencies }
                    .flatten()
                    .filter { it.group?.trim()?.isNotEmpty() ?: false }
                    .toList()
                    .forEach { dep ->
                        dependenciesNode.appendNode("dependency").apply {
                            appendNode("groupId", dep.group)
                            appendNode("artifactId", dep.name)
                            appendNode("version", dep.version)
                        }
                    }
            }
        }

        publishing {
            publications {
                create<MavenPublication>(name) {
                    groupId = Constants.GROUP_ID
                    this.artifactId = artifactId
                    this.version = version
                    artifact("$projectDir/build/outputs/aar/$name-release.aar")
                    configurePom()
                }
            }

            // Nomad-shared GitHub Maven repo
            repositories.maven {
                name = Constants.MAVEN_REPO_NAME
                url = URI.create(Constants.MAVEN_REPO_URL)
                credentials {
                    username = mavenRepoUsername
                    password = mavenRepoPassword
                }
            }
        }
    }
}
