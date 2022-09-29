package project.loader

import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.stream.Stream

object PropertyProjectLoader {

    /**
     * Regular expression to detect a property extension
     */
    private val filterRegex = Regex(".*\\.(properties|env)")

    /**
     * All loaded files
     */
    private val loadedFiles = mutableListOf<Path>()

    /**
     * Attached projects
     */
    private val cachedProjects = mutableListOf<Project>()

    /**
     * Current properties
     */
    private lateinit var cacheProperties: Properties

    /**
     * Load local properties to custom property object
     *
     * @param projectArr all projects to load
     * @return returns all result project properties
     */
    @JvmStatic
    fun loadAll(vararg projectArr: Project): Properties {
        // Initialize properties if it's required
        if (!PropertyProjectLoader::cacheProperties.isInitialized)
            cacheProperties = Properties()

        // Check if project is already on list
        for (projectItem in projectArr) {
            // Check if project already loaded
            if (projectItem in cachedProjects) continue

            // Store all porject files
            val loadFiles = getAllLoadFiles(projectItem)
            for (file in loadFiles) {
                attachFile(file, cacheProperties, projectItem)
            }

            // Insert the project on the list
            cachedProjects.add(projectItem)
        }
        // Environment variables have higher precedence than other properties
        attachEnvironment(cacheProperties)
        // Return the result object
        return cacheProperties
    }

    /**
     * Get the global project properties
     *
     * @return returns all project properties
     */
    @JvmStatic
    fun getProperties(): Properties {
        if (!PropertyProjectLoader::cacheProperties.isInitialized)
            throw IllegalAccessException("Properties is not initialized")

        return cacheProperties
    }

    /* -----------------------------------------------------
     * Internal methods
     * ----------------------------------------------------- */

    /**
     * Get all valid project property files
     *
     * @param project current project
     * @return returns a list with all property files
     */
    private fun getAllLoadFiles(project: Project): Stream<Path> {
        // Temporal variables
        val projectDir = project.projectDir.toPath()
        return Files.walk(projectDir, 1)
            .filter(PropertyProjectLoader::filterProjectFiles)
            .filter(Files::isRegularFile)
    }

    /**
     * Insert all environment values on the extra properties
     *
     * @param properties current property object
     */
    private fun attachEnvironment(properties: Properties) {
        val environment = System.getenv()
        // Iterate all elements
        for (entry in environment.entries) {
            properties[entry.key] = entry.value
        }
    }

    /**
     * Try to load properties from file
     *
     * @param file the file to load
     * @param properties target global properties
     */
    private fun attachFile(file: Path, properties: Properties, project: Project) {
        // Check if file exists
        if (!Files.exists(file) || loadedFiles.contains(file)) return

        println("Loading: \"$file\" -> :${project.name}")
        // Load configuration
        try {
            val tmpProperties = Properties()
            val tmpStream = Files.newInputStream(file)

            // Load properties
            tmpStream.use { tmpProperties.load(it) }

            // Set properties from file
            for (entry in tmpProperties.entries) {
                // Put entries
                properties[entry.key] = entry.value
            }
            // Attach to loaded files
            loadedFiles.add(file)
        } catch (e: Exception) {
            println("\"$file\" Failed. ${e.message}")
        }
    }

    /**
     * Ignore all invalid property file
     *
     * @param path the current file location to check
     */
    private fun filterProjectFiles(path: Path): Boolean =
        filterRegex.containsMatchIn(path.fileName.toString())

}