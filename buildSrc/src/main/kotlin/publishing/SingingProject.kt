package publishing

import gradle.kotlin.dsl.accessors._4ad077ad74816558e52d7069eb18a2f7.publishing
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.plugins.signing.SigningExtension
import project.BaseProject

interface SingingProject : BaseProject {

    /* -----------------------------------------------------
     * Properties
     * ----------------------------------------------------- */

    /**
     * Signing information
     */
    val signingInfo: SigningInfo?
        get() = null

    /* -----------------------------------------------------
     * Methods
     * ----------------------------------------------------- */

    /**
     * Configure all plugins per project
     *
     * @param project current project
     */
    override fun configurePluginsProject(plugins: PluginContainer, extensions: ExtensionContainer) =
        currentProject().afterEvaluate {
            // Configure the extensions
            extensions.findByType(SigningExtension::class.java)
                ?.let(this@SingingProject::configureSigningExtension)
        }

    /* -----------------------------------------------------
     * Internal methods
     * ----------------------------------------------------- */

    /**
     * Configure signing extension
     *
     * @param extension signing extension object
     */
    private fun configureSigningExtension(extension: SigningExtension) = with(extension) {
        // Check if information is null
        if (signingInfo == null) return@with
        // Configure extension
        useInMemoryPgpKeys(
            signingInfo!!.keyId,
            signingInfo!!.pgpKeyB64,
            signingInfo!!.password
        )
        // Sign
        sign(currentProject().publishing.publications)
    }

}