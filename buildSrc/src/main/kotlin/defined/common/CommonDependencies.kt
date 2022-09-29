package defined.common

import project.TypeDependency

// Common developers list
private val commonDependenciesMap: Map<Any, List<*>> = mapOf(
	TypeDependency.COMPILE_ONLY to listOf(
		"org.jetbrains:annotations:23.0.0"
	),
	TypeDependency.TEST_IMPLEMENTATION to listOf(
		"junit:junit:4.13.2"
	)
)

/* -----------------------------------------------------
 * Methods
 * ----------------------------------------------------- */

/**
 * Get common dependencies with the other elements
 *
 * @param dependencies extra dependencies
 * @return all dependencies with the extra elements
 */
fun dependencyOf(vararg dependencies: Pair<Any, List<*>>): Map<Any, List<*>> {
	val result = commonDependenciesMap.toMutableMap()

	// Iterate all elements
	for (item in dependencies) {
		// Check if key exists
		if (item.first in result) {
			result[item.first] = result[item.first]!! + item.second
			continue
		}
		// Set the content directly
		result[item.first] = item.second
	}

	return result
}