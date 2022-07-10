import conf.LocalLoader
import conf.Projects

plugins {
	id("common-java-library")
	id("common-maven-publication")
}

val env get() = LocalLoader.loadAll(rootProject)

/* -----------------------------------------------------
 * Configure tasks
 * ----------------------------------------------------- */

tasks.named("javadoc", Projects.SimpleINI::configureJavadoc)

/* -----------------------------------------------------
 * Custom tasks
 * ----------------------------------------------------- */

tasks.create("jSources", Jar::class) {
	archiveClassifier.set("sources")
	from(sourceSets["main"].java.srcDirs)
}

tasks.create("jDocs", Jar::class) {
	archiveClassifier.set("javadoc")
	from(project.tasks["javadoc"])
	dependsOn(project.tasks["javadoc"])
}

/* -----------------------------------------------------
 * Maven publications
 * ----------------------------------------------------- */

afterEvaluate {
	publishing {
		publications {
			// Release publication
			create<MavenPublication>("release") {
				Projects.SimpleINI.configurePublication(
					this,
					mapOf(
						"sourceJavadoc" to tasks["jSources"],
						"component" to components["java"],
						"javadoc" to tasks["jDocs"]
					)
				)
			}
			// Pre-release publications
		}
	}
}

/* -----------------------------------------------------
 * Publishing
 * ----------------------------------------------------- */

afterEvaluate {
	signing {
		useInMemoryPgpKeys(
			env["SIGNING_KEY_ID"] as String,
			env["SIGNING_REF_B64"] as String,
			env["SIGNING_PASSWORD"] as String
		)
		sign(publishing.publications)
	}
}