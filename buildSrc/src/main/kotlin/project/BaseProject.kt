package project

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import project.loader.PropertyProjectLoader
import java.util.*

interface BaseProject {

    /**
     * Project name
     */
    val projectName: String?

    /**
     * project group id
     */
    val projectGroup: String?

    /**
     * project artifact name
     */
    val artifactId: String?

    /**
     * project version string
     */
    val artifactVersion: String?

    /* -----------------------------------------------------
     * Methods
     * ----------------------------------------------------- */

    /**
     * Configure default project configuration
     *
     * @param project current project
     */
	fun configureAll(project: Project) = with(project) {
        // Define project
        setCurrentProject(project)
        // Load environment properties and extra project properties
        PropertyProjectLoader.loadAll(rootProject, currentProject())
        // Configure base elements
        projectGroup?.let(this::setGroup)
        artifactVersion?.let(this::setVersion)
        // Call abstract project configuration
        configurePluginsProject(plugins, extensions)
        configureProject()
        configureTaskProject(tasks)
        // Last configure dependencies
        configureDependencies(dependencies)
    }

    /**
     * Returns the current project
     */
    fun currentProject(): Project

    /**
     * Define current project
     */
    fun setCurrentProject(project: Project)

    /**
     * project dependency map
     *
     * @return All dependency map
     */
    fun dependencyMap(): Map<Any, List<*>?>? = null

    /**
     * Configure all plugins per project
     *
     * @param project current project
     */
    fun configurePluginsProject(plugins: PluginContainer, extensions: ExtensionContainer)

    /**
     * Configure all task per project
     *
     * @param project current project
     */
    fun configureTaskProject(tasks: TaskContainer)

    /**
     * Abstract project configuration
     */
    fun configureProject()

    /* -----------------------------------------------------
     * Internal methods
     * ----------------------------------------------------- */

    /**
     * Configure all project dependendencies
     *
     * @param handler Dependency handler
     */
    private fun configureDependencies(handler: DependencyHandler) = with(handler) {
        val dependencyMap = dependencyMap() ?: return@with
        // Implement dependencies
        for (entry in dependencyMap.entries) {
            // Ignore null values
            if (entry.value == null) continue
            for (dependency in entry.value!!) {
                // Ignore null dependencies
                if (dependency == null) continue
                // Check if dependency is an optional object
                when (dependency) {
                    is Optional<*> -> if (dependency.isEmpty) continue else addValidDependency(
                        entry.key,
                        dependency.get(),
                        handler
                    )

                    else -> addValidDependency(entry.key, dependency, handler)
                }
            }
        }
    }

    /* -----------------------------------------------------
     * Utility methods
     * ----------------------------------------------------- */

    /**
     * Insert a valid dependency into the project
     *
     * @param configuration dependency configuration
     * @param dependency dependency resource
     * @param handler project dependency handler
     */
    private fun addValidDependency(configuration: Any, dependency: Any, handler: DependencyHandler) = with(handler) {
        var configurationName = when (configuration) {
            is CharSequence -> configuration.toString()
            is TypeDependency -> configuration.entry
            else -> return@with
        }
        add(configurationName, dependency)
    }

    /**
     * get environment or property value
     *
     * @param name property name
     * @return returns the property value or null if not exists
     */
    fun getEnv(name: String): String? =
        PropertyProjectLoader.getProperties().getProperty(name, null)

    /**
     * get environment or property value
     *
     * @param name property name
     * @param default default value if property not exists
     * @return returns the property value or [default] if not exists
     */
    fun getEnv(name: String, default: String): String =
        PropertyProjectLoader.getProperties().getProperty(name, default)

    /**
     * Get current project child project
     *
     * @param root base project
     * @param query subproject name
     * @return Returns a project instance or [Optional.empty] if not exists
     */
    fun getSubproject(root: Project, query: String): Optional<Project> {
        return try {
            Optional.of(root.project(query))
        } catch (e: Exception) {
            e.printStackTrace()
            Optional.empty()
        }
    }

    /**
     * Returns a project from root project
     *
     * @param project target project query
     * @return Returns a project instance or [Optional.empty] if not exists
     */
    fun getProject(query: String): Optional<Project> {
        return getSubproject(currentProject(), query)
    }

}