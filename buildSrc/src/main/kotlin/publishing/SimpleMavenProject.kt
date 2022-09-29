package publishing

import gradle.kotlin.dsl.accessors._64acc05bf1a66f2c855e386526b4bcff.sourceSets
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomLicense
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.TaskContainer
import org.gradle.jvm.tasks.Jar

interface SimpleMavenProject : SingingProject {

    /* -----------------------------------------------------
     * Properties
     * ----------------------------------------------------- */

    /**
     * All maven publications
     */
    val registeredPublications: List<PublicationInfo>?

    /**
     * Generate automatically extra maven tasks
     */
    val autoGenerateMavenTasks: Boolean
        get() = false

    /* -----------------------------------------------------
     * Methods
     * ----------------------------------------------------- */

    /**
     * Configure all plugins per project
     *
     * @param project current project
     */
    override fun configurePluginsProject(plugins: PluginContainer, extensions: ExtensionContainer) {
        currentProject().afterEvaluate {
            // Configure publication config
            extensions.findByType(PublishingExtension::class.java)
                ?.let(this@SimpleMavenProject::configurePublishingExtension)
        }
        // Call parent method
        super.configurePluginsProject(plugins, extensions)
    }

    /**
     * Configure all task per project
     *
     * @param project current project
     */
    override fun configureTaskProject(tasks: TaskContainer) = with(tasks) {
        // Check if option is enabled
        if (!autoGenerateMavenTasks) return@with
        // Create custom tasks
        create("jar_javadoc", Jar::class.java, this@SimpleMavenProject::configureJarJavadocTask)
        create("jar_javasources", Jar::class.java, this@SimpleMavenProject::configureJarSourceTask)
    }

    /* -----------------------------------------------------
     * Internal methods
     * ----------------------------------------------------- */

    /**
     * Configure all maven publications
     *
     * @param extension maven publication extension
     */
    private fun configurePublishingExtension(extension: PublishingExtension) = with(extension) {
        publications {
            // Iterate all publications
            registeredPublications?.forEach {
                create(it.name, MavenPublication::class.java) {
                    configurePublicationItem(this, it)
                }
            }
        }
    }

    /**
     * Configure an individual publication
     *
     * @param publication current publication
     * @param info the publication information object
     */
    private fun configurePublicationItem(publication: MavenPublication, info: PublicationInfo) = with(publication) {
        var tmpVersion = info.version ?: artifactVersion
        // Base configuration
        groupId = info.groupId ?: projectGroup
        if (info.isSnapshot) {
            if (tmpVersion != null)
                tmpVersion = tmpVersion + "-SNAPSHOT"
        }
        version = tmpVersion
        artifactId = info.artifactId ?: this@SimpleMavenProject.artifactId

        // Configure artifact component
        currentProject().components
            .findByName(info.component)
            ?.let(this::from)

        // Configure extra artifacts
        listOf(info.javadoc.javadocTask, info.javadoc.sourceTask).forEach {
            // Check if element is null
            if (it == null) return@forEach
            // Configure extras
            currentProject().tasks
                .findByName(it)
                ?.let(this::artifact)
        }

        // Configure publication pom
        pom {
            configurePublicationPom(this, info)
        }
    }

    /**
     * Configure all pom publication
     *
     * @param pom current pom object
     * @param info publication info
     */
    private fun configurePublicationPom(pom: MavenPom, info: PublicationInfo) = with(pom) {
        // Base configuration
        url.set(info.pom.artifactUrl)
        name.set(info.pom.artifactId ?: artifactId)
        info.pom.description?.let(description::set)

        // Configure licenses
        licenses {
            info.pom.licenses?.forEach {
                license { configureLicensePublicationItem(this, it) }
            }
        }

        // Configure developers
        developers {
            info.pom.developers?.forEach {
                developer { configureDeveloperPublicationItem(this, it) }
            }
        }

        // SCM connections
        info.pom.scm?.let {
            var infoConnection = it
            // Check if url is empty
            if (infoConnection.url.isBlank())
                infoConnection = ScmConnection(info.pom.artifactUrl, infoConnection.branch)

            scm {
                url.set(infoConnection.validUrl())
                connection.set(infoConnection.validConnection())
                developerConnection.set(infoConnection.validConnection(true))
            }
        }
    }

    /**
     * Configure publication license
     *
     * @param license current license pom object
     * @param info license info
     */
    private fun configureLicensePublicationItem(license: MavenPomLicense, info: PublicationLicense) = with(license) {
        name.set(info.name)
        url.set(info.url)
    }

    /**
     * Configure developer item
     *
     * @param developer developer pom object
     * @param info developer info
     */
    private fun configureDeveloperPublicationItem(developer: MavenPomDeveloper, info: PublicationDeveloper) =
        with(developer) {
            // Required information
            id.set(info.id)
            // Optional information
            info.organizationUrl?.let(organizationUrl::set)
            info.organization?.let(organization::set)
            info.timezone?.let(timezone::set)
            info.email?.let(email::set)
            info.name?.let(name::set)
            info.url?.let(url::set)
            info.roles?.let(roles::set)
        }

    /* -----------------------------------------------------
     * Tasks
     * ----------------------------------------------------- */

    /**
     * Configure javadoc jar
     *
     * @param task current jar task
     */
    private fun configureJarJavadocTask(task: Jar) = with(task) {
        archiveClassifier.set("javadoc")
        currentProject().tasks
            .findByName("javadoc")
            ?.let {
                from(it)
                dependsOn(it)
            }
    }

    /**
     * Configure java source jar
     *
     * @param task current jar task
     */
    private fun configureJarSourceTask(task: Jar) = with(task) {
        archiveClassifier.set("sources")
        currentProject().sourceSets
            .findByName("main")
            ?.let { from(it.java.srcDirs) }
    }

}