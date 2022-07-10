package conf

import org.gradle.api.component.SoftwareComponent
import org.gradle.api.publish.maven.*
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.JavadocOutputLevel
import org.gradle.external.javadoc.StandardJavadocDocletOptions

object Projects {

	object SimpleINI {

		// Project config

		const val NAME = "simple_ini"
		const val VERSION = "0.1.0"
		const val DESCRIPTION = """"""

		// Maven config

		const val GROUP_ID = "com.github.ushiosan23"
		const val ARTIFACT_ID = "simple_ini"
		const val ARTIFACT_URL = "https://github.com/Ushiosan23/simple_ini"

		// License

		const val LICENSE_TYPE = "MIT"
		const val LICENSE_URL = "${ARTIFACT_URL}/blob/main/LICENSE.md"

		// SCM

		const val SCM_URL = "${ARTIFACT_URL}/tree/main"
		const val SCM_CONNECTION = "scm:git:github.com/Ushiosan23/simple_ini.git"
		const val SCM_SSH_CONNETION = "scm:git:ssh:github.com/Ushiosan23/simple_ini.git"

		/* -----------------------------------------------------
		 * Methods
		 * ----------------------------------------------------- */

		@JvmStatic
		fun configureJavadoc(javadoc: Javadoc) = with(javadoc.options as StandardJavadocDocletOptions) {
			docTitle = "$NAME - $VERSION"
			windowTitle = "$NAME - $VERSION"
			links = listOf(
				Configs.Javadoc.javaApiUrl,
				Configs.Javadoc.jbAnnotationsApiUrl,
				Configs.Javadoc.jvmUtilitiesApiUrl
			)
			outputLevel = JavadocOutputLevel.QUIET
		}

		@JvmStatic
		fun configurePublication(
			publication: MavenPublication,
			extras: Map<String, Any>
		) = with(publication) {
			// Base configuration
			groupId = GROUP_ID
			artifactId = ARTIFACT_ID
			version = VERSION
			// Content
			extras["component"]?.let { from(it as SoftwareComponent) }
			// Artifacts
			extras["javadoc"]?.let(this::artifact)
			extras["sourceJavadoc"]?.let(this::artifact)
			// POM File
			pom(this@SimpleINI::configurePOM)

		}

		/* -----------------------------------------------------
		 * Internal methods
		 * ----------------------------------------------------- */

		private fun configurePOM(pom: MavenPom) = with(pom) {
			// Base information
			url.set(ARTIFACT_URL)
			name.set(ARTIFACT_ID)
			description.set(DESCRIPTION.trimIndent())
			// License
			licenses {
				license(this@SimpleINI::configureLicense)
			}
			// Developers
			developers(this@SimpleINI::configureDevelopers)
			// SCM
			scm(this@SimpleINI::configureScm)
		}

		private fun configureLicense(license: MavenPomLicense) = with(license) {
			name.set(LICENSE_TYPE)
			url.set(LICENSE_URL)
		}

		private fun configureDevelopers(developerSpec: MavenPomDeveloperSpec) = with(developerSpec) {
			for (item in Contributors.contributors) {
				developer {
					item["organizationUrl"]?.let(organizationUrl::set)
					item["organization"]?.let(organization::set)
					item["timezone"]?.let(timezone::set)
					item["email"]?.let(email::set)
					item["name"]?.let(name::set)
					item["url"]?.let(url::set)
					item["id"]?.let(id::set)
					item["roles"]?.let {
						val realRoles = it.split(",").map { it.trim() }
						roles.set(realRoles)
					}
				}
			}
		}

		private fun configureScm(scm: MavenPomScm) = with(scm) {
			url.set(SCM_URL)
			connection.set(SCM_CONNECTION)
			developerConnection.set(SCM_SSH_CONNETION)
		}

	}

}