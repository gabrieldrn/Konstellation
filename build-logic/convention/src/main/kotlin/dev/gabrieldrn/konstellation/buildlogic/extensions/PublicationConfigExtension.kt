package dev.gabrieldrn.konstellation.buildlogic.extensions

open class PublicationConfigExtension(
    defaultArtifactId: String = "",
) {
    var publicationName: String = ""
    var artifactId: String = defaultArtifactId
}
