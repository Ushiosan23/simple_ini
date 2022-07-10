package conf

import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.stream.Stream

object LocalLoader {

	/**
	 * Regular expression to detect a property extension
	 */
	private val filterRegex = Regex(".*\\.(properties|env)")

	/**
	 * All loaded files
	 */
	private val loadedFiles = mutableListOf<Path>()

	/**
	 * Current properties
	 */
	private lateinit var cacheProperties: Properties

	/**
	 * Load local properties to custom property object
	 *
	 * @param project Current project
	 * @param reload Inspect all files another time
	 */
	@JvmStatic
	fun loadAll(project: Project, reload: Boolean = false): Properties {
		if (!::cacheProperties.isInitialized || reload) {
			cacheProperties = Properties()
			if (loadedFiles.isNotEmpty()) loadedFiles.clear()
		}

		// Temporal variables
		val fileSet = getAllLoadFiles(project)

		// Load all elements
		attachEnvironment(cacheProperties)
		for (file in fileSet) {
			attachFile(file, cacheProperties, project)
		}

		// Return the result object
		return cacheProperties
	}

	/* -----------------------------------------------------
	 * Internal methods
	 * ----------------------------------------------------- */

	/**
	 * Get all valid project property files
	 *
	 * @param project Current project
	 * @return Returns a list with all property files
	 */
	private fun getAllLoadFiles(project: Project): Stream<Path> {
		// Temporal variables
		val projectDir = project.projectDir.toPath()
		return Files.walk(projectDir, 1)
			.filter(LocalLoader::filterProjectFiles)
			.filter(Files::isRegularFile)
	}

	/**
	 * Insert all environment values on the extra properties
	 *
	 * @param properties Current property object
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
	 * @param file The file to load
	 * @param properties Target global properties
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
	 * @param path The current file location to check
	 */
	private fun filterProjectFiles(path: Path): Boolean =
		filterRegex.containsMatchIn(path.fileName.toString())

}