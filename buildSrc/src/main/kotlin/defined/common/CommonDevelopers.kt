package defined.common

import publishing.PublicationDeveloper

// Common developers list
private val commonDevelopersContainer = listOf(
	PublicationDeveloper(
		id = "Ushiosan23",
		name = "Brian Alvarez",
		email = "haloleyendee@outlook.com",
		url = "https://github.com/Ushiosan23"
	)
)

/* -----------------------------------------------------
 * Methods
 * ----------------------------------------------------- */

/**
 * Get common developers with the other elements
 *
 * @param developers extra developers
 * @return all developers with the extra elements
 */
fun developersOf(vararg developers: PublicationDeveloper): List<PublicationDeveloper> =
	commonDevelopersContainer + developers