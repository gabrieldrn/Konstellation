import dev.gabrieldrn.konstellation.buildlogic.GROUP_ID
import dev.gabrieldrn.konstellation.buildlogic.MAVEN_REPO_NAME
import dev.gabrieldrn.konstellation.buildlogic.MAVEN_REPO_PASSWORD_ENV_KEY
import dev.gabrieldrn.konstellation.buildlogic.MAVEN_REPO_URL
import dev.gabrieldrn.konstellation.buildlogic.MAVEN_REPO_USERNAME_ENV_KEY
import dev.gabrieldrn.konstellation.buildlogic.extensions.PublicationConfigExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create

class KonstellationPublicationPlugin : Plugin<Project> {

    private fun Project.publishing(config: PublishingExtension.() -> Unit) =
        extensions.configure(config)

    override fun apply(target: Project): Unit = with(target) {

        val publicationConfig = extensions.create<PublicationConfigExtension>(
            name = "publicationConfig",
            /*args = */ name
        )

        val mavenRepoUsername = findProperty(MAVEN_REPO_USERNAME_ENV_KEY) as String?
            ?: System.getenv("GITHUB_ACTOR")

        val mavenRepoPassword = findProperty(MAVEN_REPO_PASSWORD_ENV_KEY) as String?
            ?: System.getenv("GITHUB_TOKEN")

        pluginManager.apply("org.gradle.maven-publish")

        fun MavenPublication.configurePom() {
            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                configurations
                    .asSequence()
                    .filterNot {
                        it.name.contains("test", ignoreCase = true)
                    }
                    .map { it.dependencies }
                    .flatten()
                    .filter { it.group?.trim()?.isNotEmpty() ?: false }
                    .toList()
                    .forEach { dep ->
                        dependenciesNode.appendNode("dependency").apply {
                            appendNode(
                                "groupId",
                                // Replace local group id with the proper project group id.
                                dep.group.takeIf { it != this@with.group } ?: GROUP_ID
                            )
                            appendNode("artifactId", dep.name)
                            appendNode("version", dep.version)
                        }
                    }
            }
        }

        afterEvaluate {
            publishing {
                publications {
                    create<MavenPublication>(publicationConfig.publicationName.ifEmpty { name }) {
                        groupId = GROUP_ID
                        artifactId = publicationConfig.artifactId
                        version = "${properties["project.version"]}"
                        file("$projectDir/build/outputs/aar")
                            .listFiles { f -> f.name.contains("release", ignoreCase = true) }
                            ?.first()
                            ?.let { artifact(it) }
                        configurePom()
                    }
                }

                if (!mavenRepoUsername.isNullOrEmpty() && !mavenRepoPassword.isNullOrEmpty()) {
                    repositories.maven {
                        name = MAVEN_REPO_NAME
                        url = uri(MAVEN_REPO_URL)
                        credentials {
                            username = mavenRepoUsername
                            password = mavenRepoPassword
                        }
                    }
                } else {
                    logger.warn(
                        "No username or password found for $MAVEN_REPO_NAME. " +
                                "Please set the '$MAVEN_REPO_USERNAME_ENV_KEY' " +
                                "and '$MAVEN_REPO_PASSWORD_ENV_KEY' gradle properties."
                    )
                }
            }
        }
    }
}
