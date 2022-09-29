package project

import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.JavadocOutputLevel
import org.gradle.external.javadoc.StandardJavadocDocletOptions

interface SimpleJavaProject : BaseProject {

    /**
     * minimum java version
     */
    val javaSourceVersion: JavaVersion
        get() = JavaVersion.VERSION_11

    /**
     * default java version
     */
    val javaTargetVersion: JavaVersion
        get() = JavaVersion.VERSION_11

    /**
     * main class application
     */
    val mainClassApp: String?
        get() = null

    /**
     * project javadoc information
     */
    val javadocInfo: JavadocInfo
        get() = JavadocInfo()

    /* -----------------------------------------------------
     * Methods
     * ----------------------------------------------------- */

    /**
     * Configure all plugins per project
     *
     * @param project current project
     */
    override fun configurePluginsProject(plugins: PluginContainer, extensions: ExtensionContainer) {
        extensions.findByType(JavaApplication::class.java)?.let(this::configureJavaApplication)
        extensions.findByType(JavaPluginExtension::class.java)?.let(this::configureJavaExtension)
    }

    /**
     * Configure all task per project
     *
     * @param project current project
     */
    override fun configureTaskProject(tasks: TaskContainer) {
        tasks.findByName("javadoc")?.let { this.configureJavadocTask(it as Javadoc) }
    }

    /**
     * Configure java application
     *
     * @param extension java application extension
     */
    fun configureJavaApplication(extension: JavaApplication) = with(extension) {
        projectName?.let(this::setApplicationName)
        mainClassApp?.let(this.mainClass::set)
    }

    /**
     * Configure java extension
     *
     * @param extension java plugin extension
     */
    fun configureJavaExtension(extension: JavaPluginExtension) = with(extension) {
        sourceCompatibility = javaSourceVersion
        targetCompatibility = javaTargetVersion
    }

    /**
     * Configure javadoc output configuration
     *
     * @param tasks javadoc tasks
     */
    fun configureJavadocTask(tasks: Javadoc) = with(tasks.options as StandardJavadocDocletOptions) {
        javadocInfo.title?.let(this::setDocTitle)
        javadocInfo.windowTitle?.let(this::setWindowTitle)
        javadocInfo.urls?.let(this::setLinks)

        outputLevel = javadocInfo.outputLevel
    }

    /* -----------------------------------------------------
     * Javadoc data class
     * ----------------------------------------------------- */

    /**
     * Configure javadoc infog
     */
    data class JavadocInfo(
        val title: String? = null,
        val windowTitle: String? = null,
        val outputLevel: JavadocOutputLevel = JavadocOutputLevel.VERBOSE,
        val urls: List<String>? = null
    )

}