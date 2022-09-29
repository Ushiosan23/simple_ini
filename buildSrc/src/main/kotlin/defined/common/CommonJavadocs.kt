package defined.common

// Common developers list
private val commonJavadocLinksContainer: List<String> = listOf(
	"https://docs.oracle.com/en/java/javase/11/docs/api/",
	"https://javadoc.io/doc/org.jetbrains/annotations/latest/"
)

/* -----------------------------------------------------
 * Methods
 * ----------------------------------------------------- */

/**
 * Get common developers with the other elements
 *
 * @param javadoc extra developers
 * @return all developers with the extra elements
 */
fun javadocLinksOf(vararg javadoc: String): List<String> =
	commonJavadocLinksContainer + javadoc