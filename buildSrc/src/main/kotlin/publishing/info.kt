package publishing

data class PublicationInfo(
    val name: String,
    val isSnapshot: Boolean = false,
    val groupId: String? = null,
    val artifactId: String? = null,
    val version: String? = null,
    val component: String = "java",
    val javadoc: PublicationJavadoc = PublicationJavadoc(),
    val pom: PublicationPom
)

data class PublicationJavadoc(
    val javadocTask: String? = "jar_javadoc",
    val sourceTask: String? = "jar_javasources"
)

data class PublicationPom(
    val artifactUrl: String,
    val artifactId: String? = null,
    val description: String? = null,
    val licenses: List<PublicationLicense>? = null,
    val developers: List<PublicationDeveloper>? = null,
    val scm: ScmConnection? = null
)

data class PublicationLicense(
    val name: String = "MIT",
    val url: String = "https://opensource.org/licenses/MIT"
)

data class PublicationDeveloper(
    val id: String,
    val name: String? = null,
    val email: String? = null,
    val organization: String? = null,
    val organizationUrl: String? = null,
    val roles: List<String>? = null,
    val timezone: String? = null,
    val url: String? = null
)

data class ScmConnection(
    val url: String = "",
    val branch: String = "main",
    val connection: String? = null,
    val developerConnection: String? = null
) {

    fun validUrl(): String {
        var result = url
        // Check if url ends with git and slash suffix
        listOf(".git", "/").forEach {
            if (result.endsWith(it))
                result = result.substring(0..result.length - it.length)
        }

        return "$url/tree/$branch"
    }

    fun validConnection(ssh: Boolean = false): String {
        // Check if connection exists
        if (connection != null) return connection
        // Generate connection result
        var result = url
        listOf("http://", "https://").forEach {
            if (result.startsWith(it))
                result = result.substring(it.length)
        }

        // Check if connection ends with git url
        if (!result.endsWith(".git"))
            result += ".git"

        return (if (ssh) "scm:git:ssh:" else "scm:git:") + result
    }

}

data class SigningInfo(
    val keyId: String,
    val password: String,
    val pgpKeyB64: String
)