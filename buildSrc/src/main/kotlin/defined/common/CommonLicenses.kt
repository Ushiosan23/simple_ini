package defined.common

import publishing.PublicationLicense

// Common developers list
private val commonLicensesContainer = listOf<PublicationLicense>(
	PublicationLicense(
		name = "MIT",
		url = "https://www.github.com/Ushiosan23/simple_ini/blob/main/LICENSE.md"
	)
)

/* -----------------------------------------------------
 * Methods
 * ----------------------------------------------------- */

/**
 * Get common licenses with the other elements
 *
 * @param licences extra licenses
 * @return all licenses with the extra elements
 */
fun licensesOf(vararg licences: PublicationLicense): List<PublicationLicense> =
	commonLicensesContainer + licences